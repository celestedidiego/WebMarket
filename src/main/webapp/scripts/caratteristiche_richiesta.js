import {sweetAlert} from './sweetAlert.js';

document.addEventListener("DOMContentLoaded", () => {

    console.log("DOM fully loaded and parsed")
    const span = document.getElementById('success');
    console.log(span.innerText);
    if (span.innerText === "1") {

        sweetAlert("OK", "Caratteristica modificata con successo.", "success", () => {
        });

    } else if(span.innerText === "-1") {
        sweetAlert("Errore", "Caratteristica non modificata, il nuova valore è identico al precedente.", "error", () => {
        });
    } else if(span.innerText === "2") {
        sweetAlert("OK", "Caratteristica eliminata con successo.", "success", () => {
        });
    }
});