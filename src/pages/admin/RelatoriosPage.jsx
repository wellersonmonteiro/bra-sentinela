import React, { useState, useEffect } from 'react';
import { complaintService } from '../../services/complaintService';

import './DashboardPage.css';
import './RelatoriosPage.css';

import DashboardWidgets from '../../components/admin/DashboardWidgets';
import ChannelChartWidget from '../../components/admin/ChannelChartWidget';
import DashboardKpiGrid from '../../components/admin/DashboardKpiGrid';

const RelatoriosPage = () => {
    const [kpiData, setKpiData] = useState({
        abertas: 0,
        emAnalise: 0,
        validadas: 0,
        inconsistentes: 0
    });
    const [chartData, setChartData] = useState([]);
    const [loading, setLoading] = useState(true);

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
        if (!Array.isArray(data)) return [];
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

    const getColorForChannel = (channel) => {
        const c = (channel || "").toLowerCase();
        if (c.includes('whatsapp')) return 'var(--color-green-500)';
        if (c.includes('sms')) return 'var(--color-yellow-500)';
        if (c.includes('email')) return 'var(--color-blue-500)';
        if (c.includes('ligação') || c.includes('voz')) return 'var(--color-orange-500)';
        return 'var(--color-gray-400)';
    };

    useEffect(() => {
        const loadData = async () => {
            try {
                setLoading(true);

                const listData = await complaintService.getComplaints();

                const formattedData = Array.isArray(listData) ? listData.map(item => ({
                    ...item,
                    status: item.status || item.statusComplaint || 'ABERTA'
                })) : [];

                setKpiData(calculateKpis(formattedData));
                setChartData(calculateChartData(formattedData));

            } catch (error) {
                console.error(error);
            } finally {
                setLoading(false);
            }
        };

        loadData();
    }, []);

    if (loading) return <div className="loading-screen">Carregando métricas...</div>;

    return (
        <div className="relatorios-container">
            <h1 className="main-title">Relatórios e Métricas</h1>

            <div className="reports-layout">
                <div className="reports-sidebar">
                    <DashboardWidgets />
                </div>

                <div className="reports-main">
                    <DashboardKpiGrid data={kpiData} />
                    <div style={{ marginTop: '2rem' }}>
                        <ChannelChartWidget data={chartData} />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RelatoriosPage;