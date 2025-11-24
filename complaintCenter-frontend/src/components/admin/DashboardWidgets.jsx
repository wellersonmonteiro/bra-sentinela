import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './DashboardWidgets.css';
import { complaintService } from '../../services/complaintService';
import { FileDown } from 'lucide-react';

const ReportWidget = () => {
    const [isDownloading, setIsDownloading] = useState(false);
    const [selectedPeriod, setSelectedPeriod] = useState(6);

    const handleDownload = async (e) => {
        e.preventDefault();
        setIsDownloading(true);

        try {
            await complaintService.downloadReportPdf(selectedPeriod);
        } catch (error) {
            alert('Falha ao gerar o relatório. Tente novamente.');
        } finally {
            setIsDownloading(false);
        }
    };

    return (
        <div className="widget-card">
            <h3 className="widget-title">Relatórios</h3>
            <div className="widget-content">
                <p className="widget-description">
                    Baixe o relatório analítico completo em formato PDF. Selecione o período desejado abaixo.
                </p>

                <form onSubmit={handleDownload} className="widget-form">
                    <div className="form-group">
                        <label className="form-label">Período de Análise</label>
                        <select
                            className="form-select"
                            value={selectedPeriod}
                            onChange={(e) => setSelectedPeriod(Number(e.target.value))}
                        >
                            <option value={1}>Último Mês</option>
                            <option value={3}>Últimos 3 Meses</option>
                            <option value={6}>Últimos 6 Meses</option>
                        </select>
                    </div>

                    <button
                        type="submit"
                        className="button button-primary full-width"
                        disabled={isDownloading}
                    >
                        {isDownloading ? 'Gerando PDF...' : (
                            <>
                                <FileDown size={18} style={{ marginRight: '8px' }} />
                                Baixar Relatório PDF
                            </>
                        )}
                    </button>
                </form>
            </div>
            <div className="widget-footer">
                <Link to="/relatorios">Ver dashboard completo &rarr;</Link>
            </div>
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