import {sweetAlert} from './sweetAlert.js';

$(document).ready(function () {

    // Per la Categoria Padre
    $('#sceltaCategoriaPadre').on("change", function () {
        console.log("Categoria Padre cambiata", $(this).val());
        if ($(this).val() === "0") {
            sweetAlert("Seleziona una CategoriaPadre", "Seleziona una CategoriaPadre valida", "warning");
            return
        }
        $.ajax({
            url: '/crea_richiesta',
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded',
            data: {
                categoriaPadre: $(this).val()
            },

            success: function (data) {
                $('#sceltaCategoriaFiglio').empty()
                $('#sceltaCategoriaFiglio').append('<option value="0">Seleziona una CategoriaFiglio</option>');
                $('#sceltaCategoriaNipote').empty()
                $('#sceltaCategoriaNipote').append('<option value="0">Seleziona una CategoriaNipote</option>');
                if (data.length === 0) return

                console.log(data);

                for (let i = 0; i < data.length; i++) {
                    $('#sceltaCategoriaFiglio').append('<option value=' + data[i].key + '>' + data[i].nome + '</option>');
                }
                $('#categorieFiglioSelect').removeClass('invisible');

            },
            error: function (error) {
                console.error('Error:', error);
            }
        });
    });

    // Per la Categoria Figlio
    $('#sceltaCategoriaFiglio').on("change", function () {
        console.log("Categoria Figlio cambiata", $(this).val());
        if ($(this).val() === "0") {
            sweetAlert("Seleziona una CategoriaFiglio", "Seleziona una CategoriaFiglio valida", "warning");
            return
        }

        $.ajax({
            url: '/crea_richiesta',
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded',
            data: {
                categoriaFiglio: $(this).val()
            },

            success: function (data) {
                $('#sceltaCategoriaNipote').empty()
                $('#sceltaCategoriaNipote').append('<option value="0">Seleziona una CategoriaNipote</option>');
                if (data.length === 0) return

                console.log(data);

                for (let i = 0; i < data.length; i++) {
                    $('#sceltaCategoriaNipote').append('<option value=' + data[i].key + '>' + data[i].nome + '</option>');
                }
                $('#categorieNipoteSelect').removeClass('invisible');

            },
            error: function (error) {
                console.error('Error:', error);
            }
        });
    });

    // Per la Categoria Nipote
    // Per la Categoria Figlio
    $('#sceltaCategoriaNipote').on("change", function () {
        console.log("Categoria Nipote cambiata", $(this).val());
        if ($(this).val() === "0") {
            sweetAlert("Seleziona una CategoriaNipote", "Seleziona una CategoriaNipote valida", "warning");
            return
        }

        $.ajax({
            url: '/crea_richiesta',
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded',
            data: {
                categoriaNipote: $(this).val()
            },

            success: function (data) {
                if (data.length === 0) return

                console.log(data);
                let caratteristiche = $('#caratteristiche-item');
                caratteristiche.empty()

                for(let i = 0; i < data.length; i++){
                    caratteristiche.append(
                                `<span>
                                    <input class="invisible" type="hidden" id="key" name="key" value="${data[i].key}">
                                    <h5 class="label-caratteristica">${data[i].nome}</h5>
                                    <span class="d-flex align-items-center w-100 mb-3">
                                        <input class="input-value form-control" id="${data[i].key}" name="${data[i].key}" type="text" class="form-control" placeholder="Inserisci il valore">${data[i].unitaMisura}
                                    </span>
                                </span>`);
                }
                $('#right-content').removeClass('invisible');
            },
            error: function (error) {
                console.error('Error:', error);
            }
        });
    });

});