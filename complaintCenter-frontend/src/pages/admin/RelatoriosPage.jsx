import React from 'react';

import './DashboardPage.css';
import './RelatoriosPage.css';

import DashboardWidgets from '../../components/admin/DashboardWidgets';
import ChannelChartWidget from '../../components/admin/ChannelChartWidget';
import DashboardKpiGrid from '../../components/admin/DashboardKpiGrid';

const RelatoriosPage = () => {

    const kpiData = {
        abertas: 12,
        emAnalise: 5,
        validadas: 83,
        inconsistentes: 21
    };

    return (
        <>
            <h1 className="main-title">Relatórios</h1>

            <div className="reports-layout">
                <DashboardWidgets />
                <DashboardKpiGrid data={kpiData} />
                <ChannelChartWidget />
            </div>
        </>
    );
};

export default RelatoriosPage;