import {sweetAlert} from './sweetAlert.js';

document.addEventListener("DOMContentLoaded", () => {
    let span = document.getElementById('success');
    if (span.innerText === "1") {
        sweetAlert("OK", "Caratteristica modificata con successo.", "success", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });
    } else if (span.innerText === "2") {
        sweetAlert("OK", "Caratteristica aggiunta con successo.", "success", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });
    } else if (span.innerText === "-2") {
        sweetAlert("Errore", "Seleziona una categoria nipote.", "error", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });
    }
});