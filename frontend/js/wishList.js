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
function href(page) {
  window.location.href = page;
}
async function addWishList(id) {
  const url = `http://localhost:8080/wishList/delete?courseId=${id}`;
  await $.ajax({
    url: url,
    method: "POST",
    xhrFields: {
      withCredentials: true,
    },
  });
  getWishList();
}

//願望清單資料
async function getWishList() {
  $("#divResults").empty();
  await $.ajax({
    url: "http://localhost:8080/wishList/get",
    method: "GET",
    xhrFields: {
      withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
    },
  })
    .done((data) => {
      if (data != 0) {
        $.each(data, function (idx, item) {
          $("#divResults").append(`
        <div
              class="min-w-80 m-2  ml-10 bg-white flex flex-col items-center group cursor-pointer relative"
            >        
            <a href="./course-details.html?course_id=${item.courseId}">
              <div class="max-w-80 max-h-44 min-w-80 min-h-44 overflow-hidden border-2">
                <img
                  src="${item.courseImg}"
                  class="object-cover group-hover:scale-105 duration-300 w-full h-full"
                />
              </div>
              <div class="w-80 py-4 px-2">
                <p class="text-lg font-bold max-h-14 overflow-hidden">
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
              <div class="text-4xl text-end absolute bottom-2 right-1 z-1">
               <i onclick="addWishList(${
                 item.courseId
               })" id="wish${item.courseId}" class="wish bi bi-heart-fill text-red-400 hover:text-gray-200 duration-300 mr-2 cursor-pointer"></i>
               </div> 
               </div>
            `);
        });
      } else {
        $("#divResults").append(`<div class="flex flex-col w-full items-center">
              <p class="text-4xl mb-12 font-bold">尚未加入課程至願望清單</p>
            </div>`);
      }
    })
    .fail(() => {
      window.location.href = "../index.html";
    });
}
async function islogin() {
  const isLoginResponse = await fetch("http://localhost:8080/user/islogin", {
    method: "GET",
    credentials: "include",
  });
  const isLoggedIn = await isLoginResponse.text();
  if (isLoggedIn != "true") {
    window.location.href = "../index.html";
  } else {
    getWishList();
  }
}
islogin();
