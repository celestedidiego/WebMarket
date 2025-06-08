import {sweetAlert} from './sweetAlert.js';

document.addEventListener("DOMContentLoaded", () => {
    let span = document.getElementById('success');
    if (span.innerText === "1") {
        sweetAlert("Ordine creato", "L'ordine è stato creato con successo.", "success", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });
    }
});