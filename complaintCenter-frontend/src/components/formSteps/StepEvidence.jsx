import React, { useState, useRef } from 'react';
import { useForm } from '../../context/FormContext';
import { Trash2, FileText, Plus, Image as ImageIcon } from 'lucide-react';
import './StepEvidence.css';

const MAX_FILES = 3;
const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
const ALLOWED_FILE_TYPES = ['image/jpeg', 'image/png', 'application/pdf'];

function StepEvidence({ prevStep, loading }) {
    const { formData, setFormData } = useForm();
    const [fileError, setFileError] = useState(null);
    const fileInputRef = useRef(null);

    const currentFiles = formData.files || [];

    const handleAddClick = () => {
        fileInputRef.current.click();
    };

    const handleFileChange = (e) => {
        setFileError(null);
        const selectedFiles = Array.from(e.target.files);

        e.target.value = null;

        if (selectedFiles.length === 0) return;

        if (currentFiles.length + selectedFiles.length > MAX_FILES) {
            setFileError(`Você só pode enviar no máximo ${MAX_FILES} arquivos no total.`);
            return;
        }

        const filePromises = selectedFiles.map(file => {
            return new Promise((resolve, reject) => {
                if (file.size > MAX_FILE_SIZE) {
                    return reject(`O arquivo "${file.name}" excede o limite de 5MB.`);
                }
                if (!ALLOWED_FILE_TYPES.includes(file.type)) {
                    return reject(`O tipo do arquivo "${file.name}" não é permitido.`);
                }

                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => resolve(reader.result);
                reader.onerror = (error) => reject(error);
            });
        });

        Promise.all(filePromises)
            .then(newBase64Files => {
                setFormData(prev => ({
                    ...prev,
                    files: [...(prev.files || []), ...newBase64Files]
                }));
            })
            .catch(err => {
                setFileError(typeof err === 'string' ? err : "Erro ao processar arquivos.");
            });
    };

    const removeFile = (indexToRemove) => {
        setFormData(prev => ({
            ...prev,
            files: prev.files.filter((_, index) => index !== indexToRemove)
        }));
    };

    const isImage = (base64String) => {
        return base64String.startsWith('data:image');
    };

    return (
        <div className="form-step">
            <h2>Evidências (Opcional)</h2>
            <p>Anexe prints, fotos ou PDFs que comprovem a denúncia. Máximo de 3 arquivos.</p>

            <div className="evidence-container">

                <div className="upload-area">

                    {currentFiles.map((fileBase64, index) => (
                        <div key={index} className="file-card">
                            <button
                                type="button"
                                className="remove-btn"
                                onClick={() => removeFile(index)}
                                title="Remover arquivo"
                            >
                                <Trash2 size={14} />
                            </button>

                            {isImage(fileBase64) ? (
                                <img src={fileBase64} alt={`Evidência ${index + 1}`} className="file-preview-img" />
                            ) : (
                                <div className="file-preview-icon">
                                    <span className="pdf-label">PDF</span>
                                    <FileText size={32} />
                                    <span style={{fontSize: '10px', marginTop: '5px'}}>Documento {index + 1}</span>
                                </div>
                            )}
                        </div>
                    ))}

                    {currentFiles.length < MAX_FILES && (
                        <button type="button" className="add-file-btn" onClick={handleAddClick}>
                            <Plus className="add-icon" />
                            <span className="add-text">Adicionar</span>
                        </button>
                    )}
                </div>

                <div className="file-counter">
                    {currentFiles.length} de {MAX_FILES} arquivos adicionados.
                </div>

                {fileError && <div className="alert-box error">{fileError}</div>}

                <input
                    type="file"
                    ref={fileInputRef}
                    style={{ display: 'none' }}
                    multiple
                    onChange={handleFileChange}
                    accept=".jpg, .jpeg, .png, .pdf"
                />
            </div>

            <div className="step-actions">
                <button type="button" className="cta-button secondary" onClick={prevStep} disabled={loading}>Voltar</button>
                <button type="submit" className="cta-button" disabled={loading}>
                    {loading ? 'Enviando...' : 'Finalizar Denúncia'}
                </button>
            </div>
        </div>
    );
}

export default StepEvidence;