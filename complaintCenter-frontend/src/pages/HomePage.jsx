import React, { useState } from 'react';
import './HomePage.css';
import ComplaintForm from '../components/ComplaintForm';
import { ChevronDown, ChevronUp, Shield, HelpCircle, Bell } from 'lucide-react';

const InfoCardAccordion = () => {
    const [activeIndex, setActiveIndex] = useState(null);

    const toggleAccordion = (index) => {
        setActiveIndex(activeIndex === index ? null : index);
    };

    const accordionData = [
        {
            title: "O que é o Golpe da Falsa Central?",
            icon: <HelpCircle size={30} />,
            largeIcon: <HelpCircle size={60} />,
            content: "É quando criminosos se passam por funcionários do banco ou de centrais de segurança (Centrais de Atendimento, Suporte, etc.) e ligam para a vítima, geralmente informando sobre uma suposta transação suspeita ou problema na conta. O objetivo é criar pânico e convencer a vítima a fornecer dados ou realizar um PIX para a conta do golpista (a 'conta de segurança' ou 'cancelamento')."
        },
        {
            title: "Como se proteger imediatamente?",
            icon: <Shield size={30} />,
            largeIcon: <Shield size={60} />,
            content: "Se receber uma ligação questionando transações, nunca confirme ou forneça dados. Diga que irá desligar e ligar de volta para o número oficial do seu banco (geralmente impresso no verso do cartão ou no app). O golpista tentará manter você na linha, mas desligue. Espere pelo menos 5 minutos ou use um telefone fixo para ligar para a central oficial. O banco NUNCA solicita senhas ou o envio de valores para 'cancelar' operações."
        },
        {
            title: "Sinais de Alerta de que é um Golpe",
            icon: <Bell size={30} />,
            largeIcon: <Bell size={60} />,
            content: "Os criminosos costumam usar táticas de alta pressão e urgência. Sinais claros incluem: 1) Pedido para realizar um PIX ou TED para 'cancelar' uma transação. 2) Pedido para instalar qualquer tipo de aplicativo de acesso remoto. 3) Solicitação de senhas completas, códigos de segurança de cartão (CVV) ou tokens. 4) Ameaças de perda de dinheiro imediata se você não seguir as instruções deles."
        }
    ];

    return (
        <div className="info-card-section">
            <h2 className="info-section-title">Saiba mais sobre o Golpe da Falsa Central</h2>
            <div className="info-card-grid">
                {accordionData.map((item, index) => (
                    <div key={index} className={`info-card-item ${activeIndex === index ? 'active' : ''}`}>
                        <button
                            className="card-header-button"
                            onClick={() => toggleAccordion(index)}
                            aria-expanded={activeIndex === index}
                        >
                            <div className="card-icon-container">
                                {item.icon}
                            </div>
                            <h4 className="card-title">{item.title}</h4>
                            <div className="card-chevron">
                                {activeIndex === index ? <ChevronUp size={24} /> : <ChevronDown size={24} />}
                            </div>
                        </button>

                        {activeIndex !== index && (
                            <div className="card-placeholder">
                                {item.largeIcon}

                            </div>
                        )}

                        <div className="card-content-wrapper">
                            <div className="card-content">
                                {item.content}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};


function HomePage() {
    const [showForm, setShowForm] = useState(false);
    return (
        <>
            <div className="homepage-container">
                <div className="main-content">
                    <div className="text-column">
                        <header className="page-header">
                            <h1>Central de Denúncias: <span className="realce-header">Golpe da Falsa Central</span></h1>
                        </header>
                        {!showForm ? (
                            <>
                                <section className="intro-section">
                                    <p>
                                        Registre sua denúncia se você passou por alguma das seguintes situações:
                                    </p>
                                </section>
                                <section className="scenarios-list">
                                    <ul>
                                        <li>
                                            Recebeu contato sobre uma suposta transação indevida.
                                        </li>
                                        <li>
                                            Pediram que você realizasse uma transação (PIX, TED, boleto).
                                        </li>
                                        <li>
                                            Pressionaram você para agir imediatamente, usando um falso senso de urgência ou medo.
                                        </li>
                                        <li>
                                            Solicitaram senhas, códigos de acesso ou dados completos da conta/cartão.
                                        </li>
                                    </ul>
                                </section>
                                <div className="alert-box warning">
                                    Atenção: <strong>NUNCA</strong> ligamos
                                    pedindo senhas, dados do cartão ou a instalação de aplicativos.
                                </div>
                                <div className="alert-box info">
                                    <strong>ℹ️ Em perigo agora?</strong> Se você está{' '}
                                    <strong>atualmente em uma ligação</strong> suspeita,{' '}
                                    <strong>desligue imediatamente</strong>.
                                </div>
                                <section className="cta-section">
                                    <button className="cta-button" onClick={() => setShowForm(true)}>
                                        Iniciar Denúncia Agora
                                    </button>
                                </section>
                            </>
                        ) : (
                            <ComplaintForm />
                        )}
                    </div>
                    <div className="image-column">
                    </div>
                </div>
            </div>

            {!showForm && <InfoCardAccordion />}
        </>
    );
}

export default HomePage;