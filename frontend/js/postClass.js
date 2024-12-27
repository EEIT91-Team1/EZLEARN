const progressSteps = document.querySelectorAll(
  ".progress-step"
);
const progress = document.getElementById("progress");
let formStepsNum = 0;
const formSteps = document.querySelectorAll(".form-step");

function step2() {
  fetch("./post-class-step2.html")
    .then((response) => response.text())
    .then((data) => {
      $("#content").html(data);
    });
  formStepsNum++;
  updateProgressBar();
  console.log(formStepsNum);
}
function step3() {
  fetch("./post-class-step3.html")
    .then((response) => response.text())
    .then((data) => {
      $("#content").html(data);
    });
  formStepsNum++;
  updateProgressBar();
  console.log(formStepsNum);
}

function updateProgressBar() {
  progressSteps.forEach((progStep, index) => {
    console.log(index);

    if (index < formStepsNum + 1) {
      progStep.classList.add("active");
    } else {
      progStep.classList.remove("active");
    }
  });
  const progressActive = document.querySelectorAll(
    ".progress-step.active"
  );
  console.log(progress);

  progress.style.width =
    ((progressActive.length - 1) /
      (progressSteps.length - 1)) *
      100 +
    "%";
  console.log(progressActive);
}
function updatFormSteps(params) {
  formSteps.forEach((formStep) => {
    formStep.classList.contains("active") &&
      formStep.classList.remove("active");
  });
  formSteps[formStepsNum].classList.add("active");
}

let dataInputCount = 1;
let divCount = 1;
function plusInput() {
  // dataInputCount= parseInt( dataInputCount);
  // if (isNaN(dataInputCount)) {
  //   dataInputCount = 0;
  // }
  divCount++;
  dataInputCount++;
  let placeHoderText = `第${divCount}章 章節名稱`;

  $("#formRange")
    .append(`<div id="dataInput${dataInputCount}">
                <div
                    class="flex  focus-within:outline-black col-span-2 relative  items-center rounded-md bg-white  outline outline-1 -outline-offset-1 outline-gray-300  px-4 gap-x-1">
                    <i class="hamburger-icon bi bi-list text-4xl focus:outline-none" data-id="1"></i>
                    <input class="w-[80%]  h-[3rem] text-3xl bi bi-caret-down   outline-none  " placeholder="${placeHoderText}">
                    <button type="button" class=" absolute  right-6 w-12 flex focus:outline-none">
                        <i class="bi bi-plus-square-dotted text-4xl focus:outline-none" onclick="plusInput(dataInputCount)"></i>
                        <i class="bi bi-trash text-4xl focus:outline-none" onclick="removeInput(${dataInputCount})"></i>
                    </button>
                </div>
                <div class="ml-auto w-[80%] flex  focus-within:outline-black col-span-2 relative  items-center rounded-md bg-white  outline outline-1 -outline-offset-1 outline-gray-300  px-4 gap-x-1 ">
                    <input class="w-full  h-[3rem] text-3xl bi bi-caret-down  outline-none   " placeholder="請上傳課程影片">
                </div>
                </div>`);
  // console.log('新增'+dataInputCount);
  // console.log(typeof(dataInputCount));
  console.log("新增" + divCount);
}

function removeInput(dataInputId) {
  $(`#dataInput${dataInputId}`).remove();
  console.log(dataInputId);
  divCount--;
  console.log("刪除" + divCount);
  updatePlaceholders();
}

function updatePlaceholders() {
  $('[id^="dataInput"]').each(function (index, element) {
    // 每次遍歷到一個符合條件的元素時，執行的回調
    console.log("遍歷到 id 為 " + $(element).attr("id"));

    // 假設你想要更新這些元素內的 input 元素的 placeholder
    let newPlaceholder = "第" + (index + 1) + "章 章節名稱";
    $(element)
      .find("input:first")
      .attr("placeholder", newPlaceholder);
  });
}
