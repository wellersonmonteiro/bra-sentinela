import React from 'react';
import './DashboardWidgets.css';
import { buildReportFilename, downloadAndSaveReport } from '../../services/reportService';


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
            const filenameBase = buildReportFilename(reportParams);
            await downloadAndSaveReport(reportParams, filenameBase);
        } catch (error) {
            console.error('Erro ao baixar relatório:', error);
            alert(error?.message || 'Falha ao gerar o relatório.');
        } finally {
            setLoading(false);
        }
    };

    // Download handling moved to `reportService` to keep component clean.

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