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

        .message-proposta-rifiutata {
            color: red;
        }
    </style>
</head>
<body>

<div class="container">
    <header>
        <div>
            <h1>MyWebmarket</h1>
        </div>
        <hr></hr>
    </header>
    <#if proposta.statoProposta == "Rifiutato">
        <h1 class="message-proposta-rifiutata"><b>Proposta rifiutata</b></h1>
        <hr></hr>
    <#elseif proposta.statoProposta == "Accettato">
        <h1><b>Proposta accettata</b></h1>
        <hr></hr>
    </#if>
    <h3>Riepilogo Proposta</h3>
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
    <div>Richiesta: ${proposta.richiestaPresaInCarico.richiesta.codiceRichiesta}</div>
    <div>Tecnico dei Preventivi: ${proposta.richiestaPresaInCarico.tecnicoPreventivi.email}</div>
    <hr></hr>
    <h3>Richiesta</h3>
    <div>Richiesta: ${proposta.richiestaPresaInCarico.richiesta.codiceRichiesta}</div>
    <div>Data: ${proposta.richiestaPresaInCarico.richiesta.data}</div>
    <div>Email: ${proposta.richiestaPresaInCarico.richiesta.ordinante.email}</div>
    <div>Note: ${proposta.richiestaPresaInCarico.richiesta.note}</div>
    <hr></hr>
    <h3>Caratteristiche</h3>
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col" class="left">Nome</th>
            <th scope="col">Valore</th>
            <th scope="col">Unità di Misura</th>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list proposta.richiestaPresaInCarico.richiesta.caratteristicheConValore as caratteristicaConValore>
            <tr>
                <td class="left">${caratteristicaConValore.caratteristica.nome}</td>
                <td>${caratteristicaConValore.valore}</td>
                <td>${caratteristicaConValore.caratteristica.unitaMisura}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</div>

</body>
</html>
