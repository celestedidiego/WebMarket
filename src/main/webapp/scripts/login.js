import {sweetAlert} from './sweetAlert.js';

document.addEventListener("DOMContentLoaded", () => {
    const queryString = window.location.search
    const urlParams = new URLSearchParams(queryString);
    const errorURL = urlParams.get("error")

    switch (errorURL) {
        case "1":
            sweetAlert("Errore", "Sessione scaduta, effettuare nuovamente il login!", "error")
            break
        case "2":
            // Se email o password errate
            sweetAlert("Errore", "Email o password errate!", "error")
            break
        case "3":
            // Se email o password sono vuote
            sweetAlert("Errore", "Inserire email e password!", "error")
            break
    }
});


