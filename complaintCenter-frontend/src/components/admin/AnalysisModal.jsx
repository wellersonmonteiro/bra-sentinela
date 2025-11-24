import React, { useState, useEffect } from 'react';
import './AnalysisModal.css';
import { complaintService } from '../../services/complaintService';
import { X, FileText, User, MapPin, DollarSign, Calendar, Clock, Image as ImageIcon } from 'lucide-react';

const formatCurrency = (value) => {
    if (!value) return 'R$ 0,00';
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
};

const getBase64Src = (base64String) => {
    if (!base64String) return '';
    if (base64String.startsWith('data:image')) return base64String;
    return `data:image/png;base64,${base64String}`;
};

const AnalysisModal = ({ complaint, onClose }) => {
    const [statusComplaint, setStatusComplaint] = useState('');
    const [internalComment, setInternalComment] = useState('');
    const [complaintMessage, setComplaintMessage] = useState('');

    const [fullDetails, setFullDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        const identifier = complaint?.displayProtocol || complaint?.id;

        if (identifier) {
            setLoading(true);
            complaintService.getComplaintByProtocol(identifier)
                .then(data => {
                    setFullDetails(data);
                    const currentStatus = data.status || data.statusComplaint || 'ABERTA';
                    setStatusComplaint(currentStatus.toUpperCase());
                })
                .catch(err => console.error(err))
                .finally(() => setLoading(false));
        }
    }, [complaint]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);

        const updateData = {
            statusComplaint,
            internalComment,
            complaintMessage,
            files: []
        };

        try {
            const result = await complaintService.updateComplaintStatus(complaint.id, updateData);

            if (result.success) {
                alert('Análise salva com sucesso!');
                onClose(true);
            } else {
                throw new Error(result.message || 'Erro ao salvar');
            }
        } catch (error) {
            alert(`Falha: ${error.message}`);
        } finally {
            setIsSubmitting(false);
        }
    };

    if (!complaint) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="modal-header">
                    <div>
                        <h2 className="modal-title">Analisar Denúncia: <span className="highlight">{complaint.displayProtocol || complaint.protocol}</span></h2>
                        <span className="modal-subtitle">Veja os detalhes abaixo e registre sua tratativa.</span>
                    </div>
                    <button onClick={() => onClose(false)} className="modal-close-button">
                        <X size={24} />
                    </button>
                </div>

                <div className="modal-body-scroll">
                    {loading ? (
                        <div className="loading-block">Carregando detalhes da ocorrência...</div>
                    ) : fullDetails ? (
                        <div className="details-section">
                            <h4 className="section-title">Dados da Ocorrência</h4>

                            <div className="details-grid">
                                <div className="detail-item full-width">
                                    <span className="label"><FileText size={14}/> Descrição</span>
                                    <p className="value-box">{fullDetails.description || fullDetails.descriptionComplaint}</p>
                                </div>

                                <div className="detail-item">
                                    <span className="label"><User size={14}/> Golpista</span>
                                    <p className="value">{fullDetails.attackerName || "Não informado"}</p>
                                </div>

                                <div className="detail-item">
                                    <span className="label"><DollarSign size={14}/> Valor Envolvido</span>
                                    <p className="value">{formatCurrency(fullDetails.value)}</p>
                                </div>

                                <div className="detail-item">
                                    <span className="label"><Calendar size={14}/> Data do Ocorrido</span>
                                    <p className="value">{fullDetails.date || "N/A"}</p>
                                </div>

                                <div className="detail-item">
                                    <span className="label"><Clock size={14}/> Hora Aproximada</span>
                                    <p className="value">{fullDetails.time || "N/A"}</p>
                                </div>

                                <div className="detail-item">
                                    <span className="label"><MapPin size={14}/> Local</span>
                                    <p className="value">
                                        {fullDetails.locationCity?.city} - {fullDetails.locationCity?.state}
                                    </p>
                                </div>

                                <div className="detail-item">
                                    <span className="label"><MapPin size={14}/> Canal</span>
                                    <p className="value badge">{fullDetails.channel}</p>
                                </div>
                            </div>

                            {fullDetails.files && fullDetails.files.length > 0 && (
                                <div className="evidence-section">
                                    <h5 className="subsection-title"><ImageIcon size={14}/> Evidências Anexadas ({fullDetails.files.length})</h5>
                                    <div className="evidence-grid">
                                        {fullDetails.files.map((fileBase64, index) => (
                                            <div key={index} className="evidence-item">
                                                <img
                                                    src={getBase64Src(fileBase64)}
                                                    alt={`Evidência ${index + 1}`}
                                                    className="evidence-img"
                                                    onClick={() => window.open(getBase64Src(fileBase64), '_blank')}
                                                />
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>
                    ) : (
                        <div className="error-block">Não foi possível carregar os detalhes.</div>
                    )}

                    <hr className="divider" />

                    <form id="analysis-form" onSubmit={handleSubmit} className="analysis-form">
                        <h4 className="section-title">Registrar Análise</h4>

                        <div className="form-group">
                            <label className="form-label">Classificação (Status)</label>
                            <select
                                value={statusComplaint}
                                onChange={(e) => setStatusComplaint(e.target.value)}
                                className="form-select"
                            >
                                <option value="ABERTA">Aberta</option>
                                <option value="EM_ANALISE">Em Análise</option>
                                <option value="VALIDADA">Validada (Procedente)</option>
                                <option value="INCONSISTENTE">Inconsistente (Improcedente)</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <label className="form-label">Comentário Interno (Equipe)</label>
                            <textarea
                                rows="3"
                                value={internalComment}
                                onChange={(e) => setInternalComment(e.target.value)}
                                placeholder="Análise técnica, observações para outros analistas..."
                                className="form-input"
                            />
                        </div>

                        <div className="form-group">
                            <label className="form-label">Mensagem ao Usuário (Público)</label>
                            <textarea
                                rows="3"
                                value={complaintMessage}
                                onChange={(e) => setComplaintMessage(e.target.value)}
                                placeholder="Essa mensagem aparecerá na tela de acompanhamento do usuário..."
                                className="form-input highlight-input"
                            />
                        </div>
                    </form>
                </div>

                <div className="modal-footer">
                    <button type="button" onClick={() => onClose(false)} className="button button-cancel">
                        Cancelar
                    </button>
                    <button type="submit" form="analysis-form" disabled={isSubmitting || loading} className="button button-primary">
                        {isSubmitting ? 'Salvando...' : 'Salvar Análise'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default AnalysisModal;