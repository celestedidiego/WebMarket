import {sweetAlert} from './sweetAlert.js';

document.addEventListener("DOMContentLoaded", () => {
    console.log("DOM fully loaded and parsed")
    let span = document.getElementById('success');
    console.log(span.innerText);
    if (span.innerText === "1") {

        sweetAlert("Modifica effettuata", "Categoria Figlio modificata con successo.", "success", () => {
        });

    } else if (span.innerText === "2") {
        sweetAlert("Aggiunta effettuata", "Categoria Figlio aggiunta con successo.", "success", () => {
        });

    } else if (span.innerText === "-1") {

        sweetAlert("Errore", "Categoria Figlio non aggiunta, scegli una categoria padre.", "error", () => {
        });

    }
});