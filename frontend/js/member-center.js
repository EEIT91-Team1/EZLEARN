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
      <div class="m-4 min-w-72 max-w-72 ">
          <img
            src="data:image/png;base64,${item.courses.courseImg}"
            class="h-44 w-72 object-cover border-2"
            alt=""
          />
          <p class="text-xl">${item.courses.courseName}</p>
          <p class="text-gray-500">${item.courses.userInfo.userName}</p>
          <div class="h-4 w-full border-2 rounded-md">
            <div class="h-full bg-green-100" style="width: 70%"></div>
          </div>
          完成進度：70%
        </div>`);
  });
});
