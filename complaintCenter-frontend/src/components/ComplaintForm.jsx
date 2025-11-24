import React, { useState } from 'react';
import { FormProvider, useForm } from '../context/FormContext';
import './ComplaintForm.css';
import { registerUser, registerComplaint } from '../services/complaintService';

import StepIdentification from './formSteps/StepIdentification';
import StepUserDetails from './formSteps/StepUserDetails';
import StepComplaintDetails from './formSteps/StepComplaintDetails';
import StepEvidence from './formSteps/StepEvidence';
import StepSuccess from './formSteps/StepSuccess';

const formatDateToBackend = (isoDate) => {
    if (!isoDate) return "";
    const [year, month, day] = isoDate.split('-');
    return `${day}/${month}/${year}`;
};

const cleanValueToString = (value) => {
    if (!value) return "0";
    return value.toString().replace("R$", "").replace(/\s/g, "").replace(",", ".");
};

function ComplaintFormOrchestrator() {

    const { formData } = useForm();
    const [currentStep, setCurrentStep] = useState(1);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [protocol, setProtocol] = useState(null);

    const nextStep = () => {
        setCurrentStep((prev) => prev + 1);
    };

    const skipToStep = (stepNumber) => {
        setCurrentStep(stepNumber);
    };

    const prevStep = () => {
        if (currentStep === 3 && formData.isAnonymous) {
            setCurrentStep(1);
        } else {
            setCurrentStep((prev) => prev - 1);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        let customerIdString = null;

        try {
            if (!formData.isAnonymous) {

                const userPayload = {
                    fullName: formData.name,
                    email: formData.email
                };

                const userResponse = await registerUser(userPayload);
                const rawId = userResponse.id || userResponse.userId;
                customerIdString = rawId ? String(rawId) : null;
            }

            const complaintPayload = {
                description: formData.description,
                date: formatDateToBackend(formData.date),
                time: formData.time,
                channel: formData.channel,
                attackerName: formData.attackerName,
                value: cleanValueToString(formData.value),
                locationCity: {
                    city: formData.city,
                    state: formData.state
                },
                files: (formData.files || []).map(base64String => base64String.split(',')[1]),
                customerId: customerIdString,
            };

            const complaintResponse = await registerComplaint(complaintPayload);
            setProtocol(complaintResponse.protocol);
            setCurrentStep(5);

        } catch (err) {
            console.error(err);
            setError('Erro ao processar o envio. Tente novamente.');
        } finally {
            setLoading(false);
        }
    };

    const renderStep = () => {
        switch (currentStep) {
            case 1:
                return <StepIdentification
                    nextStep={nextStep}
                    skipToStep={skipToStep}
                />;
            case 2:
                return <StepUserDetails nextStep={nextStep} prevStep={prevStep} />;
            case 3:
                return <StepComplaintDetails nextStep={nextStep} prevStep={prevStep} />;
            case 4:
                return <StepEvidence prevStep={prevStep} loading={loading} />;
            case 5:
                return <StepSuccess protocol={protocol} />;
            default:
                return null;
        }
    };

    return (
        <form className="complaint-form" onSubmit={handleSubmit}>
            {error && <div className="alert-box error">{error}</div>}
            {renderStep()}
        </form>
    );
}

function ComplaintForm() {
    return (
        <FormProvider>
            <ComplaintFormOrchestrator />
        </FormProvider>
    );
}

export default ComplaintForm;