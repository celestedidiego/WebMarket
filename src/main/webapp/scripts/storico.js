import {sweetAlert} from './sweetAlert.js';

document.addEventListener("DOMContentLoaded", () => {

    console.log("DOM fully loaded and parsed")
    const span = document.getElementById('success');
    console.log(span.innerText);
    if (span.innerText === "1") {

        sweetAlert("OK", "Feedback inserito con successo.", "success", () => {
        });

    }
});