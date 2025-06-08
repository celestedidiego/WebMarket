import {sweetAlert} from './sweetAlert.js';

document.addEventListener("DOMContentLoaded", () => {


    let tbody = document.getElementById('tbody');
    let rows = Array.from(tbody.rows);

    // Fa il sort in base al numero presente nella prima cella della riga, 0 se è consegnato, 1 altrimenti
    rows.sort((b, a) => a.cells[0].innerText - b.cells[0].innerText);

    for (let row of rows) {
        tbody.removeChild(row);
    }
    for (let row of rows) {
        tbody.appendChild(row);
    }

    let span = document.getElementById('success');
    if (span.innerText === "1") {
        sweetAlert("Stato dell'ordine modificato", "Lo stato dell'ordine è: In Consegna.", "success", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });
    }
    if (span.innerText === "2") {
        sweetAlert("Stato dell'ordine modificato", "Lo stato dell'ordine è: Consegnato.", "success", () => {
            // This is done to avoid the resubmission of the form
            window.location.href = window.location.pathname + window.location.search
        });
    }
});