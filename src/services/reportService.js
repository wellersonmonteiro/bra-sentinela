import api from './api';
import { complaintService } from './complaintService';

/**
 * reportService: small helper library that centralizes download + blob
 * validation logic for reports (CSV / PDF). This keeps components thin and
 * makes the download behavior easier to maintain and test.
 */

/**
 * Download a report (CSV or PDF) using the backend client and save it to disk.
 * reportParams is passed directly to `complaintService.downloadReport`.
 * filenameBase should be provided without extension; function appends correct
 * extension based on MIME type returned by the server.
 */
export async function downloadAndSaveReport(reportParams, filenameBase) {
  const { type, start, end } = reportParams || {};

  // resumo_mensal -> backend PDF endpoint
  if (type === 'resumo_mensal') {
    let months = 6;
    try {
      if (start && end) {
        const s = new Date(start);
        const e = new Date(end);
        const diffMs = Math.abs(e - s);
        months = Math.max(1, Math.ceil(diffMs / (30 * 24 * 60 * 60 * 1000)));
      }
    } catch (err) {
      months = 6;
    }

    const resp = await api.get('/v1/report/last-months/pdf', { params: { months }, responseType: 'blob' });
    await saveBlobFromResponse(resp.data, filenameBase);
    return;
  }

  // export_csv -> backend CSV endpoint; fallback to client CSV if backend fails
  if (type === 'export_csv') {
    const params = {};
    if (start) params.start = start;
    if (end) params.end = end;

    try {
      const resp = await api.get('/v1/report/complaints/csv', { params, responseType: 'blob' });
      await saveBlobFromResponse(resp.data, filenameBase);
      return;
    } catch (err) {
      console.warn('Backend CSV generation failed, falling back to client CSV', err?.message || err);
      // fallback: build CSV client-side from complaints
      const complaints = await complaintService.getComplaints(params);

      const headers = ['id', 'createdAt', 'channel', 'status', 'location', 'description'];
      const csvRows = [headers.join(',')];

      complaints.forEach((c) => {
        const row = [
          `"${c.id ?? ''}"`,
          `"${c.createdAt ?? ''}"`,
          `"${(c.channel ?? '').toString().replace(/"/g, '""')}"`,
          `"${c.status ?? ''}"`,
          `"${c.location ?? ''}"`,
          `"${(c.description ?? '').toString().replace(/"/g, '""')}"`
        ];
        csvRows.push(row.join(','));
      });

      const csv = csvRows.join('\n');
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
      await saveBlobFromResponse(blob, filenameBase);
      return;
    }
  }

  // For other report types, call the generic JSON report endpoint and save as JSON file
  const resp = await api.get('/v1/report', { params: { type, start, end }, responseType: 'json' });
  const jsonBlob = new Blob([JSON.stringify(resp.data, null, 2)], { type: 'application/json' });
  await saveBlobFromResponse(jsonBlob, filenameBase);
}

/**
 * Convenience when you already have a Blob/ArrayBuffer response and just want
 * to validate the response and trigger a download.
 */
export async function saveBlobFromResponse(blobOrData, baseName) {
  const blob = blobOrData instanceof Blob ? blobOrData : new Blob([blobOrData]);

  // Basic MIME validation: if server returned JSON or HTML it's probably an error
  const type = (blob.type || '').toLowerCase();
  const isJson = type.includes('application/json');
  const isHtml = type.includes('text/html');
  const isCsv = type.includes('text/csv');

  if (isJson || isHtml) {
    const text = await blob.text();
    try {
      const obj = JSON.parse(text);
      const msg = obj.message || obj.error || JSON.stringify(obj);
      throw new Error(msg);
    } catch (e) {
      // not JSON — forward plain text
      throw new Error(text);
    }
  }

  const ext = isCsv ? 'csv' : (type.includes('pdf') ? 'pdf' : 'bin');
  const filename = `${baseName}.${ext}`;

  // Some servers respond without Content-Type; in that case fallback to octet-stream
  const toDownload = blob.type ? blob : new Blob([blob], { type: 'application/octet-stream' });
  const url = window.URL.createObjectURL(toDownload);
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', filename);
  document.body.appendChild(link);
  link.click();
  link.remove();
  window.URL.revokeObjectURL(url);
}

/**
 * Simple helper to fetch KPI/JSON reports.
 */
export async function getReport(params) {
  const resp = await api.get('/v1/report', { params });
  return resp.data;
}

export async function getLastMonthsDetailed(months = 6) {
  const resp = await api.get('/v1/report/last-months', { params: { months } });
  return resp.data;
}

/**
 * Helper to create a safe filename based on the params.
 */
export function buildReportFilename({ type, start, end }) {
  const s = start || 'inicio';
  const e = end || 'fim';
  return `relatorio_${type}_${s}_${e}`;
}

export default {
  downloadAndSaveReport,
  saveBlobFromResponse,
  buildReportFilename,
};
