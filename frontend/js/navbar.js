$("#iconNotify").click(() => {
  if ($("#divNotify").hasClass("opacity-0")) {
    $("#divNotify").removeClass("opacity-0 scale-95");
    $("#divNotify").addClass("opacity-100 scale-100");
  } else {
    $("#divNotify").addClass("opacity-0 scale-95");
    $("#divNotify").removeClass("opacity-100 scale-100");
  }

  $("#notifyCount").remove();

  $.ajax({
    url: "http://localhost:8080/notify/checked",
    method: "GET",
    xhrFields: {
      withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
    },
  }).done((data) => {
    console.log(data);
  });
});
