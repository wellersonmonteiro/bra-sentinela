import React from 'react';
import './DashboardWidgets.css';
import { complaintService } from '../../services/complaintService';


const ReportWidget = () => {
    const [loading, setLoading] = React.useState(false);

    const handleDownload = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const reportParams = {
            type: formData.get('report_type'),
            start: formData.get('start_date'),
            end: formData.get('end_date'),
        };

        setLoading(true);
        try {
            const blob = await complaintService.downloadReport(reportParams);
            // validate blob before download
            await safeDownloadBlob(blob, `relatorio_${reportParams.type}_${reportParams.start || 'inicio'}_${reportParams.end || 'fim'}`);
        } catch (error) {
            console.error('Erro ao baixar relatório:', error);
            alert(error?.message || 'Falha ao gerar o relatório.');
        } finally {
            setLoading(false);
        }
    };

    async function safeDownloadBlob(blobOrData, baseName) {
        // blobOrData may be a Blob or something else; ensure we have a Blob
        const blob = blobOrData instanceof Blob ? blobOrData : new Blob([blobOrData]);

        // if the server returned JSON or HTML (error), try to parse and show message
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
                throw new Error(text);
            }
        }

        // determine extension from MIME (allow csv explicitly)
        const ext = isCsv ? 'csv' : (type.includes('pdf') ? 'pdf' : 'bin');
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
        <div className="widget-card">
            <h3 className="widget-title">Gerar Relatório Rápido</h3>
            <form className="widget-form" onSubmit={handleDownload}>
                <div className="form-group">
                    <label htmlFor="report_type" className="form-label">Tipo de Relatório</label>
                    <select id="report_type" name="report_type" className="form-select">
                        <option value="resumo_mensal">Resumo Mensal</option>
                        <option value="por_canal">Denúncias por Canal</option>
                        <option value="status_validacao">Status de Validação</option>
                        <option value="export_csv">Exportação Completa (CSV)</option>
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="start_date" className="form-label">Data Início</label>
                    <input type="date" id="start_date" name="start_date" className="form-input" />
                </div>
                <div className="form-group">
                    <label htmlFor="end_date" className="form-label">Data Fim</label>
                    <input type="date" id="end_date" name="end_date" className="form-input" />
                </div>
                <button type="submit" className="button button-dark" disabled={loading}>
                    {loading ? 'Gerando...' : 'Gerar Relatório'}
                </button>
            </form>
        </div>
    );
};


const DashboardWidgets = () => {
    return (
        <div className="widgets-container">
            <ReportWidget />

        </div>
    );
};

export default DashboardWidgets;