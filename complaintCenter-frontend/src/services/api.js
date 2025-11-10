import axios from 'axios';

const commonHeaders = {
    'Content-Type': 'application/json',
};


export const userApi = axios.create({
    baseURL: import.meta.env.VITE_API_USER_URL,
    headers: commonHeaders
});

export const complaintApi = axios.create({
    baseURL: import.meta.env.VITE_API_COMPLAINT_URL,
    headers: commonHeaders
});

