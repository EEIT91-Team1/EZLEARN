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
  if (data.avatar !== "noImg") {
    $("#userAvatar").prop("src", data.avatar);
  }

  $("#userName").text(data.userName);
  $("#email").text(data.email);
});

$("#btnProgress").on("click", function () {
  if ($(this).hasClass("bi-chevron-down")) {
    $(this).removeClass("bi-chevron-down");
    $(this).addClass("bi-chevron-up");
    $("#divResult").removeClass("hidden");
    $("#divResult").addClass("flex");
  } else {
    $(this).addClass("bi-chevron-down");
    $(this).removeClass("bi-chevron-up");
    $("#divResult").removeClass("flex");
    $("#divResult").addClass("hidden");
  }
});

$.ajax({
  url: "http://localhost:8080/progress/user",
  method: "GET",
  xhrFields: {
    withCredentials: true,
  },
}).done((data) => {
  console.log(data);
  let totalPersentage = 0;
  let completedCourse = 0;
  $.each(data, (idx, item) => {
    $("#results").append(
      `<a href="http://127.0.0.1:5500/pages/lecture.html?course_id=${item.courseId}" class="m-4 group">        
      <div class=" min-w-60 max-w-60 flex flex-col justify-between"> 
          <img
            src="${item.courseImg}"
            class="h-40 w-60 object-cover border-2"
            alt=""
          />
          <p class="text-lg">${item.courseName}</p>
          <p class="text-gray-500">${item.teacher}</p>
          <div><div class="h-2 w-full border-2 rounded-md">
            <div class="h-full bg-indigo-600" style="width: ${item.completedPercentage}%"></div>
          </div>
          完成進度：<span class="completedPercentage">${item.completedPercentage}%</span>
        </div></div>
              </a>`
    );
    if (item.completedPercentage == 100) {
      completedCourse += 1;
    }
    totalPersentage += parseInt(item.completedPercentage);
  });
  totalPersentage = parseInt(totalPersentage / data.length);
  $("#totalPersentage").text(totalPersentage + "%");
  $("#totalCourse").text(data.length);
  $("#completedCourse").text(completedCourse);
  $("#persent").css("width", totalPersentage + "%");
  $("#pPersent").text(totalPersentage + "%");
  $("#pPersent").css("left", totalPersentage + "%");
});
