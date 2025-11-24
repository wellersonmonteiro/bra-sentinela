import api from './api';

export const registerUser = async (userData) => {
    try {
        const response = await api.post('/v1/user', userData);
        return response.data;
    } catch (error) {
        console.error("Erro ao registrar usuário:", error.response || error.message);
        throw error;
    }
};

export const registerComplaint = async (complaintData) => {
    try {
        const response = await api.post('/v1/complaint', complaintData);
        return response.data;
    } catch (error) {
        console.error("Erro ao registrar denúncia:", error.response || error.message);
        throw error;
    }
};

export const getComplaintByProtocol = async (protocolId) => {
    try {
        const response = await api.get(`/v1/complaint/${protocolId}`);
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar denúncia:", error.response || error.message);
        throw error;
    }
};

export const getReport = async () => {
    try {
        const response = await api.get('/v1/report');
        return response.data;
    } catch (error) {
        console.error('Erro ao buscar relatório:', error.response || error.message);
        throw error;
    }
};

export const getLastMonthsDetailed = async (months = 6) => {
    try {
        const response = await api.get('/v1/report/last-months', { params: { months } });
        return response.data;
    } catch (error) {
        console.error('Erro ao buscar relatório detalhado:', error.response || error.message);
        throw error;
    }
};

export const downloadReportPdf = async (months = 6) => {
    try {
        const response = await api.get('/v1/report/last-months/pdf', {
            params: { months },
            responseType: 'blob'
        });
        return response.data;
    } catch (error) {
        console.error('Erro ao baixar PDF do relatório:', error.response || error.message);
        throw error;
    }
};

/**
 * Download a report based on form parameters.
 * Currently supports `resumo_mensal` (monthly summary) which maps
 * to the `/v1/report/last-months/pdf` endpoint. Other types will
 * throw an error until backend endpoints are implemented.
 */
export const downloadReport = async ({ type, start, end } = {}) => {
    // Only support two types for now: resumo_mensal (PDF) and export_csv (CSV)
    if (type === 'resumo_mensal') {
        // months derived from range if provided, otherwise default to 6
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

        return downloadReportPdf(months);
    }

    if (type === 'export_csv') {
        // Prefer backend CSV generation. Call gateway endpoint which proxies to complaint-service.
        const params = {};
        if (start) params.start = start;
        if (end) params.end = end;

        try {
            const response = await api.get('/v1/report/complaints/csv', { params, responseType: 'blob' });
            return response.data;
        } catch (err) {
            console.warn('Backend CSV generation failed, falling back to client CSV', err?.message || err);
            // fallback to client-side CSV generation if backend fails
            const complaints = await getComplaints(params);

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
            return new Blob([csv], { type: 'text/csv;charset=utf-8;' });
        }
    }

    throw new Error('Tipo de relatório não suportado: ' + type);
};


const getComplaints = async (filters) => {
    const params = new URLSearchParams();

    if (filters && filters.type) params.append('tipo', filters.type);
    if (filters && filters.date) params.append('data', filters.date);
    if (filters && filters.location) params.append('local', filters.location);
    if (filters && filters.status) params.append('status', filters.status);
    if (filters && filters.start) params.append('start', filters.start);
    if (filters && filters.end) params.append('end', filters.end);

    try {
        const response = await api.get('/v1/complaint', { params });
        return response.data;
    } catch (error) {
        console.error('Erro ao buscar denúncias:', error);
        return [];
    }
};

const updateComplaintStatus = async (complaintId, analysisData) => {
    try {
        const response = await api.put(`/v1/analysis/${complaintId}`, analysisData);
        if (response.status === 203) {
            return { success: true, message: response.data.message };
        }
        return { success: false };
    } catch (error) {
        console.error('Error updating status:', error);
        return { success: false, error: error.message };
    }
};

export const complaintService = {
    getComplaints,
    updateComplaintStatus,
    getReport,
    getLastMonthsDetailed,
    downloadReportPdf,
    downloadReport
};