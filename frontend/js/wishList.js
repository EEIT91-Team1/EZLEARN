function rateToStars(rate) {
  let stars =
    '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i>';
  if (rate < 4.75 && rate >= 4.25) {
    stars =
      '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-half"></i>';
  } else if (rate < 4.25 && rate >= 3.75) {
    stars =
      '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star"></i>';
  } else if (rate < 3.75 && rate >= 3.25) {
    stars =
      '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-half"></i><i class="bi bi-star"></i>';
  } else if (rate < 3.25 && rate >= 2.75) {
    stars =
      '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
  } else if (rate < 2.75 && rate >= 2.25) {
    stars =
      '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-half"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
  } else if (rate < 2.25 && rate >= 1.75) {
    stars =
      '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
  } else if (rate < 1.75 && rate >= 1.25) {
    stars =
      '<i class="bi bi-star-fill"></i><i class="bi bi-star-half"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
  } else if (rate < 1.25) {
    stars =
      '<i class="bi bi-star-fill"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
  } else if (rate == 0 || rate == "n") {
    stars =
      '<i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
  }
  return stars;
}

$(document).ready(function () {
  $.ajax({
    url: "http://localhost:8080/wishList/get",
    method: "GET",
    xhrFields: {
      withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
    },
  })
    .done((data) => {
      $.each(data, function (idx, item) {
        $("#divResults").append(`
        <div
              class="min-w-80 m-2 mr-auto bg-white flex flex-col items-center group cursor-pointer"
            >        
            <a href="./course-details.html?course_id=${item.courseId}">
              <div class="max-w-80 max-h-44 min-w-80 min-h-44 overflow-hidden border-2">
                <img
                  src="${item.courseImg}"
                  class="object-cover group-hover:scale-105 duration-300 w-full h-full"
                />
              </div>
              <div class="w-80 p-4">
                <p class="text-lg font-bold max-h-14">
                  ${item.courseName}
                </p>
                <p class="text-gray-600">${item.teacher}</p>
                <p>
                ${rateToStars(item.rate)}
                (${item.rate})
                </p>
                <p class="text-xl font-semibold">$${item.price}</p>
              </div>
            </a>
            </div>
            `);
      });
    })
    .fail(() => {
      window.location.href = "../index.html";
    });
});
