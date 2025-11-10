import { userApi, complaintApi } from './api';


export const registerUser = async (userData) => {
    try {
        const response = await userApi.post('/v1/user', userData);
        return response.data;
    } catch (error) {
        console.error("Erro ao registrar usuário:", error.response || error.message);
        throw error;
    }
};


export const registerComplaint = async (complaintData) => {
    try {
        const response = await complaintApi.post('/v1/complaint', complaintData);
        return response.data;
    } catch (error) {
        console.error("Erro ao registrar denúncia:", error.response || error.message);
        throw error;
    }
};