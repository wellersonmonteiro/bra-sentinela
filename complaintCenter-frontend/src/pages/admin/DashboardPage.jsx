import React, { useState, useEffect } from 'react';
import { getComplaints } from '../../services/complaintService';
import './DashboardPage.css';

import DashboardKpiGrid from '../../components/admin/DashboardKpiGrid';
import ComplaintList from '../../components/admin/ComplaintList';
import AnalysisModal from '../../components/admin/AnalysisModal';
import DashboardWidgets from '../../components/admin/DashboardWidgets';
import ChannelChartWidget from '../../components/admin/ChannelChartWidget';

const DashboardPage = () => {
    const [kpiData, setKpiData] = useState({ abertas: 0, emAnalise: 0, validadas: 0, inconsistentes: 0 });
    const [complaints, setComplaints] = useState([]);
    const [chartData, setChartData] = useState([]);
    const [selectedComplaint, setSelectedComplaint] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [loading, setLoading] = useState(true);

    const getColorForChannel = (channel) => {
        const c = (channel || "").toLowerCase();
        if (c.includes('whatsapp')) return 'var(--color-green-500)';
        if (c.includes('sms')) return 'var(--color-yellow-500)';
        if (c.includes('email') || c.includes('e-mail')) return 'var(--color-blue-500)';
        if (c.includes('ligação') || c.includes('voz')) return 'var(--color-orange-500)';
        return 'var(--color-gray-400)';
    };

    const calculateKpis = (data) => {
        const counts = { abertas: 0, emAnalise: 0, validadas: 0, inconsistentes: 0 };

        data.forEach(item => {
            const status = (item.status || item.statusComplaint || "").toLowerCase();

            if (status.includes('aberta') || status === 'open') counts.abertas++;
            else if (status.includes('analise') || status === 'in_analysis') counts.emAnalise++;
            else if (status.includes('validada') || status === 'validated') counts.validadas++;
            else if (status.includes('inconsistente') || status === 'inconsistent') counts.inconsistentes++;
            else counts.abertas++;
        });
        return counts;
    };

    const calculateChartData = (data) => {
        const channelCounts = {};
        const total = data.length;

        data.forEach(item => {
            const channel = item.channel || "Outros";
            channelCounts[channel] = (channelCounts[channel] || 0) + 1;
        });

        return Object.keys(channelCounts).map(key => ({
            name: key,
            percent: total === 0 ? 0 : Math.round((channelCounts[key] / total) * 100),
            color: getColorForChannel(key)
        }));
    };

    const loadDashboardData = async () => {
        setLoading(true);
        try {
            const apiData = await getComplaints();

            const formattedData = Array.isArray(apiData) ? apiData.map(item => ({
                id: item.id || item.complaintId,
                displayProtocol: item.protocolNumber || item.protocol || "S/N",
                createdAt: item.createdDate || item.date,
                channel: item.channel,
                status: item.status || item.statusComplaint || 'ABERTA'
            })) : [];

            formattedData.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

            setKpiData(calculateKpis(formattedData));
            setChartData(calculateChartData(formattedData));

            const recentComplaints = formattedData.slice(0, 5);
            setComplaints(recentComplaints);

        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadDashboardData();
    }, []);

    const handleAnalyzeClick = (complaint) => {
        setSelectedComplaint(complaint);
        setIsModalOpen(true);
    };

    const handleCloseModal = (shouldReload = false) => {
        setIsModalOpen(false);
        setSelectedComplaint(null);
        if (shouldReload) {
            loadDashboardData();
        }
    };

    if (loading) return <div className="loading-screen">Carregando Dashboard...</div>;

    return (
        <>
            <h1 className="main-title">Painel do Analista</h1>
            <DashboardKpiGrid data={kpiData} />
            <ChannelChartWidget data={chartData} />

            <div className="main-grid">
                <div className="main-column">
                    <ComplaintList
                        complaints={complaints}
                        onAnalyzeClick={handleAnalyzeClick}
                        title="Últimas Denúncias"
                    />
                </div>

                <div className="sidebar-column">
                    <DashboardWidgets />
                </div>
            </div>

            {isModalOpen && (
                <AnalysisModal
                    complaint={selectedComplaint}
                    onClose={handleCloseModal}
                />
            )}
        </>
    );
};

export default DashboardPage;