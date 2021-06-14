$(document).ready(function () {
    showItems(false);
});

function showItems(hasAllItems) {
    $.ajax({
        type: "GET",
        url: 'http://localhost:8080//job4j_todo/items',
        dataType: "json",
        success: function (respData) {

            let items = "";
            for (let i = 0; i < respData.length; i++) {
                let curId = respData[i]['id'];
                let curDescription = respData[i]['description'];
                let curCreated = respData[i]['created'];
                let hasCurDone = respData[i]['done'];

                if (hasAllItems) {
                    if (hasCurDone) {
                        items += `<tr>
                                          <td><input type="checkbox" name="place" value=${curId} checked></td>
                                          <td>${curDescription}</td>
                                          <td>${curCreated}</td>
                                      </tr>`;
                    } else {
                        items += `<tr>
                                          <td><input type="checkbox" name="place" value=${curId}></td>
                                          <td>${curDescription}</td>
                                          <td>${curCreated}</td>
                                      </tr>`;
                    }
                } else {
                    if (!hasCurDone) {
                        items += `<tr>
                                          <td><input type="checkbox" name="place" value=${curId}></td>
                                          <td>${curDescription}</td>
                                          <td>${curCreated}</td>
                                      </tr>`;
                    }
                }
            }

            $('#tbodyId').html(items);
        },
        error: function (err) {
            alert(err);
        }
    })
}

function validateAndCreate() {
    let valid = true;
    let description = document.getElementById('description').value;
    if (description === '') {
        valid = false;
        alert("Пожалуйста, заполните поле \"Задача\"");
    } else {
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080//job4j_todo/item',
            data: {description: description},
            success: function (respData) {
                alert("New task created!");
                location.reload();
            },
            error: function (err) {
                alert(err);
                console.log(err);
                valid = false;
            }
        })
    }
    return valid;
}

$(document).ready(function() {
    $('#showNotDone').click(function () {
        if ($(this).is(':checked')) {
            showItems(true);
        } else {
            showItems(false);
        }
    });
});

// </script>
//
// <!--    <script>-->
// <!--        function changeDone() {-->
//
// <!--            let result = true;-->
// <!--            // let checked = [];-->
// <!--            // $('input:checkbox:checked').each(function () {-->
// <!--            //     checked.push($(this).val());-->
// <!--            // });-->
// <!--            // if (checked.length === 0) {-->
// <!--            //     result = false;-->
// <!--            //     alert("Нужно выбрать места для оформления заказа!")-->
// <!--            // } else {-->
// <!--            //     localStorage.setItem('chosenTickets', checked);-->
// <!--            //     console.log(localStorage.getItem('chosenTickets'));-->
// <!--            //     window.location.href = "http://localhost:8080//job4j_cinema/payment.html";-->
// <!--            // }-->
//
// <!--            return result;-->
// <!--        }-->
// <!--    </script>-->