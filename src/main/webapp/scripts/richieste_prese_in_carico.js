import {sweetAlert} from './sweetAlert.js';

document.addEventListener("DOMContentLoaded", () => {

    console.log("DOM fully loaded and parsed")
    const span = document.getElementById('success');
    console.log(span.innerText);
    if (span.innerText === "1") {

        sweetAlert("OK", "Proposta creata con successo.", "success", () => {
        });

    } else if (span.innerText === "-1") {

        sweetAlert("Errore", "Il prezzo inserito non è corretto!.", "error", () => {
        });

    } else if (span.innerText === "-2") {

        sweetAlert("Errore", "L'URL inserito non è valido!.", "error", () => {
        });

    }
});