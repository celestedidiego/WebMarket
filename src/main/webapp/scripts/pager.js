/* NON PIU' IN USO*/

$(document).ready(function() {
    $('#fetchCategories').click(function() {
        const pageNum = $('#page').text();
        console.log(pageNum)
        $.ajax({
            url: '/categoria_padre',
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded',
            data: {
                page: pageNum
            },
            success: function(data) {
                if(data.length === 0) return
                appendCategories(data);
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    });

    function appendCategories(categories) {

        const categoriesDiv = document.querySelector('#tbody');
        console.log(categories)
        console.log(categoriesDiv)
        let result = ''
        for(let i = 0; i < categories.length; i++) {
            let category = categories[i];
            console.log("Category: ",category)
            result += `<tr>`

            result += `<td><a href="categoria_figlio?id_categoria_genitore=${category['key']}">${category['nome']}</a></td>`
            result += `<td>
                            <form method="POST" action="categoria_padre">
                                <input type="hidden" name="id" value="${category['key']}">
                                <input class="btn btn-primary" type="submit" id="render" name="render" value="Modifica">
                            </form>
                       </td>`
            result += `<td>
                            <form method="POST" action="categoria_padre">
                                <input type="hidden" name="id" value="${category['key']}">
                                <input class="btn btn-danger" type="submit" id="action" name="action" value="Elimina">
                            </form>
                       </td>`
            result += `</tr>`
        }
        categoriesDiv.innerHTML += result;
        document.querySelector('#page').innerText = parseInt(document.querySelector('#page').innerText) + 1;
    }
});

