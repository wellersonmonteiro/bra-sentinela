import api from './api';

export const registerUser = async (userData) => {
    try {
        const response = await api.post('/v1/user', userData);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const registerComplaint = async (complaintData) => {
    try {
        const response = await api.post('/v1/complaint', complaintData);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const getComplaintByProtocol = async (protocolId) => {
    try {
        const response = await api.get(`/v1/complaint/${protocolId}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const getComplaints = async (filters) => {
    const params = new URLSearchParams();

    if (filters) {
        if (filters.type) params.append('tipo', filters.type);
        if (filters.date) params.append('data', filters.date);
        if (filters.location) params.append('local', filters.location);
        if (filters.status) params.append('status', filters.status);
    }

    try {
        const response = await api.get('/v1/complaint', { params });
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
};

export const updateComplaintStatus = async (complaintId, updateData) => {
    try {
        const payload = {
            statusComplaint: updateData.statusComplaint,
            internalComment: updateData.internalComment,
            complaintMessage: updateData.complaintMessage,
            files: updateData.files || []
        };

        const response = await api.put(`/v1/complaint/${complaintId}`, payload);

        if (response.status >= 200 && response.status < 300) {
            return { success: true, data: response.data };
        }
        return { success: false };
    } catch (error) {
        return { success: false, error: error.message };
    }
};

export const complaintService = {
    registerUser,
    registerComplaint,
    getComplaintByProtocol,
    getComplaints,
    updateComplaintStatus,
};