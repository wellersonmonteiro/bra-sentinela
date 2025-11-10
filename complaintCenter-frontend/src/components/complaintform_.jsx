// src/components/ComplaintForm.jsx

import React, { useState } from 'react';
import { FormProvider, useForm } from '../context/FormContext';
import './ComplaintForm.css';

// ... (imports dos Steps) ...

// IMPORTANTE: Importe seus novos serviços
import { registerUser, registerComplaint } from '../services/complaintService';


function ComplaintFormOrchestrator() {

    const { formData } = useForm();
    const [currentStep, setCurrentStep] = useState(1);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [protocol, setProtocol] = useState(null);

    // ... (funções nextStep, skipToStep, prevStep não mudam) ...


    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        let customerId = null; // Continua null se for anônimo

        try {
            // --- INÍCIO DA MUDANÇA (USUÁRIO) ---
            if (!formData.isAnonymous) {
                const userPayload = {
                    name: formData.name,
                    email: formData.email,
                    phone: formData.phone,
                    cpf: formData.cpf,
                };
                console.log("Enviando POST /v1/user:", userPayload);

                // SUBSTITUI O MOCK
                const userResponse = await registerUser(userPayload);
                customerId = userResponse.id; // Pega o ID da resposta real
            }
            // --- FIM DA MUDANÇA (USUÁRIO) ---


            // Monta o payload da denúncia (nenhuma mudança aqui)
            const complaintPayload = {
                description: formData.description,
                date: formData.date,
                time: formData.time,
                channel: formData.channel,
                attackerName: formData.attackerName,
                value: formData.value,
                locationCity: { city: formData.city, state: formData.state },
                files: formData.files.map(base64String => base64String.split(',')[1]),
                customerId: customerId,
            };

            // --- INÍCIO DA MUDANÇA (DENÚNCIA) ---
            console.log("Enviando POST /v1/complaint:", complaintPayload);

            // SUBSTITUI O MOCK
            const complaintResponse = await registerComplaint(complaintPayload);
            const newProtocol = complaintResponse.protocol; // Pega o protocolo real
            // --- FIM DA MUDANÇA (DENÚNCIA) ---

            setProtocol(newProtocol);
            setCurrentStep(5); // Vai para a etapa de sucesso

        } catch (err) {
            console.error(err);
            // O "catch" que já tínhamos agora vai pegar os erros do axios!
            setError('Houve um erro ao registrar sua denúncia. Tente novamente.');
        } finally {
            setLoading(false);
        }
    };

    // ... (função renderStep não muda) ...

    // ... (return do formulário não muda) ...
}

// ... (wrapper ComplaintForm e export default não mudam) ...

export default ComplaintForm;