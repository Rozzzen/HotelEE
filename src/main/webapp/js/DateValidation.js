const today = new Date();
document.getElementById('check_in').min = today.toISOString().split("T")[0];
document.getElementById('check_out').min = today.toISOString().split("T")[0];

today.setFullYear(today.getFullYear() + 1);
document.getElementById('check_in').max = today.toISOString().split("T")[0];
document.getElementById('check_out').max = today.toISOString().split("T")[0];

document.getElementById('check_in').onchange = function () {
    document.getElementById('check_out').min = this.value;
};