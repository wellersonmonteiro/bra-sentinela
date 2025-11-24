import React, { useState } from 'react';
import { getComplaintByProtocol } from '../services/complaintService';
import './TrackComplaintPage.css';

const formatDate = (isoString) => {
    if (!isoString) return 'Data indisponível';
    try {
        const date = new Date(isoString);
        return date.toLocaleString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    } catch (e) {
        return isoString;
    }
};

const formatStatus = (status) => {
    const map = {
        'OPEN': 'Aberta',
        'ABERTA': 'Aberta',
        'IN_ANALYSIS': 'Em Análise',
        'EM_ANALISE': 'Em Análise',
        'VALIDATED': 'Validada',
        'VALIDADA': 'Validada',
        'INCONSISTENT': 'Inconsistente',
        'INCONSISTENTE': 'Inconsistente'
    };
    return map[status] || status;
};

function TrackComplaintPage() {
    const [protocolInput, setProtocolInput] = useState('');
    const [complaintData, setComplaintData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!protocolInput) return;

        setLoading(true);
        setError(null);
        setComplaintData(null);

        try {
            const data = await getComplaintByProtocol(protocolInput);
            setComplaintData(data);
        } catch (err) {
            if (err.response && err.response.status === 404) {
                setError('Protocolo não encontrado. Verifique o número e tente novamente.');
            } else {
                setError('Houve um erro ao buscar sua denúncia. Tente mais tarde.');
            }
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="track-page-container">
            <header className="page-header">
                <h1>Acompanhe sua Denúncia</h1>
                <p>Insira o número do protocolo recebido para consultar o status.</p>
            </header>

            <form className="track-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="protocol">Número do Protocolo</label>
                    <input
                        type="text"
                        id="protocol"
                        value={protocolInput}
                        onChange={(e) => setProtocolInput(e.target.value)}
                        placeholder="Ex: ABC01112025"
                    />
                </div>
                <button type="submit" className="cta-button" disabled={loading}>
                    {loading ? 'Buscando...' : 'Buscar'}
                </button>
            </form>

            {error && (
                <div className="alert-box error track-result">
                    {error}
                </div>
            )}

            {complaintData && (
                <div className="track-result-card">
                    <h3>Detalhes da Denúncia</h3>

                    <div className="result-item">
                        <strong>Protocolo:</strong>
                        <span>{complaintData.protocolNumber}</span>
                    </div>

                    <div className="result-item">
                        <strong>Status:</strong>
                        <span className={`status-badge ${complaintData.statusComplaint?.toLowerCase()}`}>
                            {formatStatus(complaintData.statusComplaint)}
                        </span>
                    </div>

                    <div className="result-item">
                        <strong>Data de Criação:</strong>
                        <span>{formatDate(complaintData.createdDate)}</span>
                    </div>

                    <div className="result-item">
                        <strong>Descrição:</strong>
                        <p>{complaintData.descriptionComplaint}</p>
                    </div>

                    <div className="result-item">
                        <strong>Mensagem do Atendente:</strong>
                        <p className="message-box">
                            {complaintData.message
                                ? complaintData.message
                                : "Sua solicitação está aguardando análise."}
                        </p>
                    </div>
                </div>
            )}
        </div>
    );
}

export default TrackComplaintPage;