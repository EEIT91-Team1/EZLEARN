$(document).ready(function () {
  fetch("../components/navbar.html")
    .then((response) => response.text())
    .then((data) => {
      $("#navbar").html(data);
    });

  fetch("../components/footer.html")
    .then((response) => response.text())
    .then((data) => {
      $("#footer").html(data);
    });
});
