<!DOCTYPE html>
<html lang="it">
<head>
    <title>Template PDF</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 80%;
            margin: 0 auto;
        }

        h1 {
            color: #333;
            text-align: center;
        }

        p {
            line-height: 1.6;
            font-size: 14px;
        }

        .proposta {
            margin-top: 1rem;
        }

        header {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-bottom: 2rem;
        }

        hr {
            display: flex;
            justify-content: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 8px;
            text-align: center;
            justify-content: center;
        }

        th {
            background-color: #f2f2f2;
        }

        .left {
            text-align: left;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tr:hover {
            background-color: #e9e9e9;
        }
    </style>
</head>
<body>

<div class="container">
    <header>
        <div>
            <h1>WebMarket</h1>
        </div>
        <hr></hr>
    </header>
    <h3><b>Ordine Creato</b></h3>
    <div>Stato: <b>${ordine.statoConsegna}</b></div>
    <div>Tecnico: ${ordine.tecnico.email}</div>
    <hr></hr>
    <h3>Proposta</h3>
    <div>Proposta: ${proposta.codiceProdotto}</div>
    <div>Produttore: ${proposta.produttore}</div>
    <div>Nome Prodotto: ${proposta.nomeProdotto}</div>
    <div>Prezzo: ${proposta.prezzo}</div>
    <div>Note: ${proposta.note}</div>
    <div>Stato Proposta: ${proposta.statoProposta}</div>
    <#if proposta.motivazione??>
        <div>Motivazione: ${proposta.motivazione}</div>
    </#if>
    <div>Produttore: ${proposta.produttore}</div>
    <hr></hr>
    <h3>Richiesta presa in carico</h3>
    <div>Richiesta: ${proposta.richiestaPresaInCarico.richiestaOrdine.codiceRichiesta}</div>
    <div>Tecnico: ${proposta.richiestaPresaInCarico.tecnico.email}</div>
    <hr></hr>
    <h3>Richiesta</h3>
    <div>Richiesta: ${proposta.richiestaPresaInCarico.richiestaOrdine.codiceRichiesta}</div>
    <div>Data: ${proposta.richiestaPresaInCarico.richiestaOrdine.data}</div>
    <div>Email: ${proposta.richiestaPresaInCarico.richiestaOrdine.ordinante.email}</div>
    <div>Note: ${proposta.richiestaPresaInCarico.richiestaOrdine.note}</div>
    <hr></hr>
    <h3>Caratteristiche</h3>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Nome</th>
            <th scope="col">Valore</th>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list proposta.richiestaPresaInCarico.richiestaOrdine.caratteristicheRichiesta as caratteristicaRichiesta>
            <tr>
                <td class="left">${caratteristicaRichiesta.caratteristica.nome}</td>
                <td>${caratteristicaRichiesta.valore}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</div>

</body>
</html>
