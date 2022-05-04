M.AutoInit();

document.addEventListener('DOMContentLoaded', function() {
    const datepicker = document.querySelectorAll(".datepicker");
    M.Datepicker.init(datepicker, {
        format: 'dd-mm-yyyy',
    });
});

function addParamToUrlAndRedirect(name, param) {
    const url = new URL(window.location.href);
    url.searchParams.delete('page');
    if (param === "") {
        url.searchParams.delete(name);
    } else {
        url.searchParams.set(name, param);
    }
    window.location.href = url.href;
}