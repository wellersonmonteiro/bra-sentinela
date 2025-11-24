import React from 'react';
import './ComplaintList.css';

const formatDate = (dateString) => {
    if (!dateString) return '-';
    try {
        return new Date(dateString).toLocaleDateString('pt-BR');
    } catch { return dateString; }
};

const StatusBadge = ({ status }) => {
    const statusKey = (status || "").toLowerCase();

    const statusMap = {
        aberta: { text: 'Aberta', className: "status-blue" },
        open: { text: 'Aberta', className: "status-blue" },
        em_analise: { text: 'Em Análise', className: "status-yellow" },
        in_analysis: { text: 'Em Análise', className: "status-yellow" },
        validada: { text: 'Validada', className: "status-green" },
        validated: { text: 'Validada', className: "status-green" },
        inconsistente: { text: 'Inconsistente', className: "status-gray" },
        inconsistent: { text: 'Inconsistente', className: "status-gray" },
    };

    const s = statusMap[statusKey] || { text: status, className: "status-gray" };

    return (
        <span className={`status-badge ${s.className}`}>
            {s.text}
        </span>
    );
};

const ComplaintList = ({ complaints, onAnalyzeClick, title, showFooter = true }) => {
    return (
        <div className="complaint-list-card">
            {title && <h2 className="complaint-list-title">{title}</h2>}

            <div className="table-wrapper">
                <table className="complaint-table">
                    <thead>
                    <tr>
                        <th>Protocolo</th>
                        <th>Data</th>
                        <th>Canal</th>
                        <th>Status</th>
                        <th>Ação</th>
                    </tr>
                    </thead>
                    <tbody>
                    {complaints && complaints.length > 0 ? (
                        complaints.map((complaint) => (
                            <tr key={complaint.id}>
                                <td data-label="Protocolo" className="protocol-cell">
                                    {complaint.displayProtocol}
                                </td>
                                <td data-label="Data">
                                    {formatDate(complaint.createdAt)}
                                </td>
                                <td data-label="Canal">{complaint.channel}</td>
                                <td data-label="Status">
                                    <StatusBadge status={complaint.status} />
                                </td>
                                <td data-label="Ação">
                                    <button
                                        onClick={() => onAnalyzeClick(complaint)}
                                        className="button button-primary"
                                    >
                                        Analisar
                                    </button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5" className="empty-row">
                                Nenhuma denúncia encontrada nesta lista.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>

            {showFooter && (
                <div className="complaint-list-footer">
                    <a href="#">Ver todas as denúncias &rarr;</a>
                </div>
            )}
        </div>
    );
};

export default ComplaintList;