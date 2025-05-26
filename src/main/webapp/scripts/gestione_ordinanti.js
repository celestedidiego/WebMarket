import {sweetAlert} from './sweetAlert.js';

document.addEventListener("DOMContentLoaded", () => {
    let span = document.getElementById('success');
    if (span.innerText === "1") {
        sweetAlert("Modifica effettuata", "Ordinante modificato con successo.", "success", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });
    } else if (span.innerText === "2") {
        sweetAlert("Aggiunta effettuata", "Ordinante aggiunto con successo.", "success", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });
    } else if (span.innerText === "-2") {
        sweetAlert("Email già utilizzata", "Non è permesso l'utilizzo della stessa mail più volte", "error", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });
    }
});