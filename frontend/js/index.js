//課程推薦輪播
const recommedCarousel = document.getElementById("recommedCarousel");
const btnRecommedPrev = document.getElementById("btnRecommedPrev");
const btnRecommedNext = document.getElementById("btnRecommedNext");
const recommedPage = document.getElementById("recommedPage");
let currentRecommedPage = 1;
let currentRecommedIndex = 0;

const updateRecommedCarousel = () => {
  const offset = -currentRecommedIndex * 100;
  recommedCarousel.style.transform = `translateX(${offset}%)`;
};

btnRecommedPrev.addEventListener("click", () => {
  currentRecommedIndex =
    currentRecommedIndex > 0 ? currentRecommedIndex - 1 : 2;
  updateRecommedCarousel();
  currentRecommedPage == 1
    ? (currentRecommedPage = 3)
    : (currentRecommedPage = currentRecommedPage - 1);
  recommedPage.innerText = currentRecommedPage;
});

btnRecommedNext.addEventListener("click", () => {
  currentRecommedIndex =
    currentRecommedIndex < 2 ? currentRecommedIndex + 1 : 0;
  updateRecommedCarousel();
  currentRecommedPage == 3
    ? (currentRecommedPage = 1)
    : (currentRecommedPage = currentRecommedPage + 1);

  recommedPage.innerText = currentRecommedPage;
});
//---------------------------------------------------------------
//學員評論輪播
const reviewCarousel = document.getElementById("reviewCarousel");
const btnReviewPrev = document.getElementById("btnReviewPrev");
const btnReviewNext = document.getElementById("btnReviewNext");
const reviewPage = document.getElementById("reviewPage");
let currentReviewPage = 1;
let currentReviewIndex = 0;

const updateReviewCarousel = () => {
  const offset = -currentReviewIndex * 100;
  reviewCarousel.style.transform = `translateX(${offset}%)`;
};

btnReviewPrev.addEventListener("click", () => {
  currentReviewIndex = currentReviewIndex > 0 ? currentReviewIndex - 1 : 2;
  updateReviewCarousel();
  currentReviewPage == 1
    ? (currentReviewPage = 3)
    : (currentReviewPage = currentReviewPage - 1);
  reviewPage.innerText = currentReviewPage;
});

btnReviewNext.addEventListener("click", () => {
  currentReviewIndex = currentReviewIndex < 2 ? currentReviewIndex + 1 : 0;
  updateReviewCarousel();
  currentReviewPage == 3
    ? (currentReviewPage = 1)
    : (currentReviewPage = currentReviewPage + 1);

  reviewPage.innerText = currentReviewPage;
});

//--------------------------------------------------------
//淡入淡出動畫
const observer = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("visible");
      } else {
        entry.target.classList.remove("visible");
      }
    });
  },
  { threshold: 0.3 }
);
document.querySelectorAll(".fadeIn").forEach((el) => observer.observe(el));

//-------------------------------------------------------
//圖片動畫
window.addEventListener("scroll", () => {
  const scrollY = window.scrollY;

  const pic2 = document.getElementById("searchPic2");
  const pi2cOffsetX = scrollY * 0.15;
  pic2.style.transform = `translateX(${pi2cOffsetX}px)`;

  const slogan = document.getElementById("slogan");
  const sloganOffsetX = scrollY * 0.3;
  slogan.style.transform = `translateX(${sloganOffsetX}px)`;

  const pic1 = document.getElementById("searchPic1");
  const pic1OffsetX = scrollY * -0.1;
  pic1.style.transform = `translateX(${pic1OffsetX}px)`;

  const pic3 = document.getElementById("searchPic3");
  const pic3OffsetY = scrollY * -0.3;
  pic3.style.transform = `translateY(${pic3OffsetY}px)`;

  const bg = document.getElementById("bg1");
  const bg1OffsetY = scrollY * -0.2;
  bg1.style.transform = `translateY(${bg1OffsetY}px)`;

  const bg2 = document.getElementById("bg2");
  const bg2OffsetY = scrollY * -0.05;
  bg2.style.transform = `translateY(${bg2OffsetY}px)`;
});
