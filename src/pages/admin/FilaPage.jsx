import React, { useState, useEffect } from 'react';
import { getComplaints } from '../../services/complaintService';
import './FilaPage.css';

import ComplaintList from '../../components/admin/ComplaintList';
import AnalysisModal from '../../components/admin/AnalysisModal';

const FilaPage = () => {
    const [pendingComplaints, setPendingComplaints] = useState([]);
    const [finishedComplaints, setFinishedComplaints] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedComplaint, setSelectedComplaint] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [loading, setLoading] = useState(true);

    const loadComplaints = async () => {
        setLoading(true);
        try {
            const apiData = await getComplaints();

            const formattedData = Array.isArray(apiData) ? apiData.map(item => ({
                id: item.id || item.complaintId,
                displayProtocol: item.protocolNumber || item.protocol || "S/N",
                createdAt: item.createdDate || item.date,
                channel: item.channel,
                status: (item.status || item.statusComplaint || 'ABERTA').toUpperCase()
            })) : [];

            const pending = formattedData.filter(item =>
                item.status !== 'VALIDADA' && item.status !== 'INCONSISTENTE'
            );

            const finished = formattedData.filter(item =>
                item.status === 'VALIDADA' || item.status === 'INCONSISTENTE'
            );

            setPendingComplaints(pending);
            setFinishedComplaints(finished);

        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadComplaints();
    }, []);

    const filteredPending = pendingComplaints.filter(item => {
        if (!searchTerm) return true;
        const term = searchTerm.toLowerCase();
        return (
            item.displayProtocol.toLowerCase().includes(term) ||
            item.channel.toLowerCase().includes(term)
        );
    });

    const handleAnalyzeClick = (complaint) => {
        setSelectedComplaint(complaint);
        setIsModalOpen(true);
    };

    const handleCloseModal = (shouldReload = false) => {
        setIsModalOpen(false);
        setSelectedComplaint(null);
        if (shouldReload) {
            loadComplaints();
        }
    };

    if (loading) return <div className="loading-screen">Carregando Fila...</div>;

    return (
        <div className="fila-page-container">
            <h1 className="fila-main-title">Gerenciamento de Denúncias</h1>

            <section className="fila-section">
                <div className="fila-section-header">
                    <h2 className="fila-section-title">
                        Fila de Análise
                        <span className="count-badge">{filteredPending.length}</span>
                    </h2>

                    <input
                        type="text"
                        placeholder="Filtrar por protocolo ou canal..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="search-input"
                    />
                </div>

                <ComplaintList
                    complaints={filteredPending}
                    onAnalyzeClick={handleAnalyzeClick}
                    title={null}
                    showFooter={false}
                />
            </section>

            <section className="fila-section">
                <div className="fila-section-header">
                    <h2 className="fila-section-title">
                        Histórico de Finalizadas
                        <span className="count-badge">{finishedComplaints.length}</span>
                    </h2>
                </div>

                <ComplaintList
                    complaints={finishedComplaints}
                    onAnalyzeClick={handleAnalyzeClick}
                    title={null}
                    showFooter={false}
                />
            </section>

            {isModalOpen && (
                <AnalysisModal
                    complaint={selectedComplaint}
                    onClose={handleCloseModal}
                />
            )}
        </div>
    );
};

export default FilaPage;