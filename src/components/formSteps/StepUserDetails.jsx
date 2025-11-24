import React from 'react';
import { useForm } from '../../context/FormContext';

const maskCPF = (value) => {
    return value
        .replace(/\D/g, '')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d{1,2})/, '$1-$2')
        .replace(/(-\d{2})\d+?$/, '$1');
};

const maskPhone = (value) => {
    return value
        .replace(/\D/g, '')
        .replace(/(\d{2})(\d)/, '($1) $2')
        .replace(/(\d{5})(\d)/, '$1-$2')
        .replace(/(-\d{4})\d+?$/, '$1');
};

function StepUserDetails({ nextStep, prevStep }) {
    const { formData, setFormData } = useForm();

    const handleChangeWithMask = (e) => {
        const { name, value } = e.target;
        let maskedValue = value;

        if (name === 'cpf') {
            maskedValue = maskCPF(value);
        } else if (name === 'phone') {
            maskedValue = maskPhone(value);
        }

        setFormData((prev) => ({
            ...prev,
            [name]: maskedValue,
        }));
    };

    const handleGenericChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    return (
        <div className="form-step">
            <h2>Seus Dados (Opcional)</h2>
            <p>Preencha os campos abaixo. Seus dados serão mantidos em sigilo.</p>

            <div className="form-group special-field">
                <label htmlFor="name">Nome Completo</label>
                <input
                    type="text"
                    id="name"
                    name="name"
                    value={formData.name || ''}
                    onChange={handleGenericChange}
                />
            </div>

            <div className="form-group special-field">
                <label htmlFor="email">E-mail</label>
                <input
                    type="email"
                    id="email"
                    name="email"
                    value={formData.email || ''}
                    onChange={handleGenericChange}
                />
            </div>

            <div className="form-group special-field">
                <label htmlFor="phone">Telefone para Contato</label>
                <input
                    type="tel"
                    id="phone"
                    name="phone"
                    value={formData.phone || ''}
                    onChange={handleChangeWithMask}
                    placeholder="(00) 00000-0000"
                    maxLength={15}
                />
            </div>

            <div className="form-group special-field">
                <label htmlFor="cpf">CPF</label>
                <input
                    type="text"
                    id="cpf"
                    name="cpf"
                    value={formData.cpf || ''}
                    onChange={handleChangeWithMask}
                    placeholder="000.000.000-00"
                    maxLength={14}
                />
            </div>

            <div className="step-actions">
                <button type="button" className="cta-button secondary" onClick={prevStep}>Voltar</button>
                <button type="button" className="cta-button" onClick={nextStep}>Próximo</button>
            </div>
        </div>
    );
}

export default StepUserDetails;