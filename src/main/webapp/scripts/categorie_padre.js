import {sweetAlert} from './sweetAlert.js';

document.addEventListener("DOMContentLoaded", () => {
    let span = document.getElementById('success');
    if (span.innerText === "1") {
        sweetAlert("Modifica effettuata", "Categoria Padre modificata con successo.", "success", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });


    } else if (span.innerText === "2") {
        sweetAlert("Aggiunta effettuata", "Categoria Padre aggiunta con successo.", "success", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });


    }
});

