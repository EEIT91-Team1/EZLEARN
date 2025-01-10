async function loadNavbar() {
  const navbarResponse = await fetch("../components/navbar.html");
  const navbarHtml = await navbarResponse.text();
  $("#navbar").html(navbarHtml);
}

loadNavbar();

fetch("../components/footer.html")
  .then((response) => response.text())
  .then((data) => {
    $("#footer").html(data);
  });
