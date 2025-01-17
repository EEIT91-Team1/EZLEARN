//檢查是否登入
fetch("http://localhost:8080/user/islogin", {
  method: "get",
  credentials: "include",
})
  .then((response) => response.text())
  .then((data) => {
    if (data != "true") {
      window.location.href = "../index.html";
    }
  });

$.ajax({
  url: "http://localhost:8080/user/logindata",
  method: "GET",
  xhrFields: {
    withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
  },
}).done((data) => {
  console.log(data);
  $("#userAvatar").prop("src", data.avatar);
  $("#userName").text(data.userName);
  $("#email").text(data.email);
});
$.ajax({
  url: "http://localhost:8080/purchased-courses/my-courses",
  method: "GET",
  xhrFields: {
    withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
  },
}).done((data) => {
  console.log(data);
  $.each(data, (idx, item) => {
    $("#results").append(`            
      <div class="m-4 min-w-60 max-w-60 flex flex-col justify-between"> 
          <img
            src="data:image/png;base64,${item.courses.courseImg}"
            class="h-40 w-60 object-cover border-2"
            alt=""
          />
          <p class="text-lg">${item.courses.courseName}</p>
          <p class="text-gray-500">${item.courses.userInfo.userName}</p>
          <div><div class="h-4 w-full border-2 rounded-md">
            <div class="h-full bg-blue-100" style="width: 70%"></div>
          </div>
          完成進度：70%
        </div></div>`);
  });
});

$("#btnProgress").on("click", function () {
  if ($(this).hasClass("bi-chevron-down")) {
    $(this).removeClass("bi-chevron-down");
    $(this).addClass("bi-chevron-up");
    $("#results").removeClass("hidden");
    $("#results").addClass("flex");
  } else {
    $(this).addClass("bi-chevron-down");
    $(this).removeClass("bi-chevron-up");
    $("#results").removeClass("flex");
    $("#results").addClass("hidden");
  }
});
