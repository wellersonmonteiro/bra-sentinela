import React from 'react';
import { useEffect, useState } from 'react';

import './DashboardPage.css';
import './RelatoriosPage.css';

import DashboardWidgets from '../../components/admin/DashboardWidgets';
import ChannelChartWidget from '../../components/admin/ChannelChartWidget';
import DashboardKpiGrid from '../../components/admin/DashboardKpiGrid';
import { complaintService } from '../../services/complaintService';

const RelatoriosPage = () => {
    const [kpiData, setKpiData] = useState(null);
    const [detailed, setDetailed] = useState([]);
    const [months, setMonths] = useState(6);
    const [loadingDetailed, setLoadingDetailed] = useState(false);
    const [loadingPdf, setLoadingPdf] = useState(false);
    const [loadingCsv, setLoadingCsv] = useState(false);

    useEffect(() => {
        const load = async () => {
            try {
                const report = await complaintService.getReport();
                setKpiData({
                    abertas: report.open ?? report.abertas ?? 0,
                    emAnalise: report.pendingReview ?? report.emAnalise ?? 0,
                    validadas: report.validated ?? report.validadas ?? 0,
                    inconsistentes: report.inconsistent ?? report.inconsistentes ?? 0,
                });
            } catch (err) {
                console.warn('Falha ao carregar relatório, usando mock', err?.message || err);
                setKpiData({ abertas: 12, emAnalise: 5, validadas: 83, inconsistentes: 21 });
            }
        };

        load();
        loadDetailed(months);
    }, []);

    const loadDetailed = async (m = 6) => {
        setLoadingDetailed(true);
        try {
            const resp = await complaintService.getLastMonthsDetailed(m);
            setDetailed(Array.isArray(resp) ? resp : []);
        } catch (err) {
            console.warn('Falha ao carregar relatório detalhado', err?.message || err);
            setDetailed([]);
        } finally {
            setLoadingDetailed(false);
        }
    };

    const handleMonthsChange = (e) => {
        const m = Number(e.target.value) || 6;
        setMonths(m);
        loadDetailed(m);
    };

    // kept focused: monthly PDF and CSV export only

    const ChannelSummary = ({ complaints }) => {
        const map = {};
        (complaints || []).forEach(c => {
            const ch = c.channel || 'Desconhecido';
            map[ch] = (map[ch] || 0) + 1;
        });
        return (
            <div style={{ marginBottom: '1rem' }}>
                <strong>Por Canal:</strong>
                <ul>
                    {Object.entries(map).map(([k, v]) => (
                        <li key={k}>{k}: {v}</li>
                    ))}
                </ul>
            </div>
        );
    };

    const StatusSummary = ({ complaints }) => {
        const map = {};
        (complaints || []).forEach(c => {
            const st = c.status || 'Desconhecido';
            map[st] = (map[st] || 0) + 1;
        });
        return (
            <div style={{ marginBottom: '1rem' }}>
                <strong>Por Status:</strong>
                <ul>
                    {Object.entries(map).map(([k, v]) => (
                        <li key={k}>{k}: {v}</li>
                    ))}
                </ul>
            </div>
        );
    };

    const handleDownloadMonthlyPdf = async () => {
        setLoadingPdf(true);
        try {
            const blob = await complaintService.downloadReport({ type: 'resumo_mensal', start: null, end: null });
            await safeDownloadBlob(blob, `relatorio_resumo_${months}m`, 'pdf');
        } catch (err) {
            console.error('Erro ao baixar PDF:', err);
            alert('Erro ao baixar relatório PDF.');
        } finally {
            setLoadingPdf(false);
        }
    };

    const handleExportCsv = async () => {
        setLoadingCsv(true);
        try {
            // compute date range for months
            const end = new Date();
            const start = new Date();
            start.setMonth(start.getMonth() - months + 1);
            const startIso = start.toISOString().slice(0, 10);
            const endIso = end.toISOString().slice(0, 10);

            const blob = await complaintService.downloadReport({ type: 'export_csv', start: startIso, end: endIso });
            await safeDownloadBlob(blob, `denuncias_${startIso}_to_${endIso}`, 'csv');
        } catch (err) {
            console.error('Erro ao exportar CSV:', err);
            alert('Erro ao exportar CSV.');
        } finally {
            setLoadingCsv(false);
        }
    };

    async function safeDownloadBlob(blobOrData, baseName, forcedExt) {
        const blob = blobOrData instanceof Blob ? blobOrData : new Blob([blobOrData]);
        const type = (blob.type || '').toLowerCase();
        const isJson = type.includes('application/json');
        const isHtml = type.includes('text/html');
        const isCsv = type.includes('text/csv');

        if (isJson || isHtml) {
            const text = await blob.text();
            try {
                const obj = JSON.parse(text);
                throw new Error(obj.message || obj.error || JSON.stringify(obj));
            } catch (e) {
                throw new Error(text);
            }
        }

        const ext = forcedExt || (isCsv ? 'csv' : (type.includes('pdf') ? 'pdf' : 'bin'));
        const filename = `${baseName}.${ext}`;

        const url = window.URL.createObjectURL(blob.type ? blob : new Blob([blob], { type: 'application/octet-stream' }));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', filename);
        document.body.appendChild(link);
        link.click();
        link.parentNode.removeChild(link);
        window.URL.revokeObjectURL(url);
    }

    return (
        <>
            <h1 className="main-title">Relatórios</h1>

            <div className="reports-layout">
                <DashboardWidgets />
                <DashboardKpiGrid data={kpiData} />
                <ChannelChartWidget />
            </div>
        </>
    );
};

export default RelatoriosPage;