// const progressSteps = document.querySelectorAll(".progress-step");
// const progress = document.getElementById("progress");
if (typeof formStepsNum === "undefined") {
	var formStepsNum = 0; // 全局變數
}
if (typeof dataInputCount === "undefined") {
	var dataInputCount = 0; // 全局變數
}
if (typeof divCount === "undefined") {
	var divCount = 0; // 全局變數
}
if (typeof courseIndex === "undefined") {
	var courseIndex = 0; // 全局變數
}
// let dataInputCount = 1;
// let divCount = 0;
// const formSteps = document.querySelectorAll(".form-step");

function reinitializeProgressBar() {
	// 重新抓取進度條 DOM 元素
	const progressSteps = document.querySelectorAll(".progress-step");
	const progress = document.getElementById("progress");

	// 更新進度條狀態（可復用 updateProgressBar 邏輯）
	updateProgressBar();
}

function step1() {
	fetch("./post-class-step1.html")
		.then((response) => response.text())
		.then((data) => {
			$("#allContent").html(data);
			loadName("#coursename", "test11");
			loadName("#courseClass", "test14");
			loadName("#courseIntro", "test21");
			loadName("#courseSummory", "loadcoursesummory");
			loadName("#coursePrice", "loadcourseprice")
			loadImage();
		});

	formStepsNum--;
	updateProgressBar();

	// console.log(formStepsNum);
	saveFormToBackend();
}

function step2() {
	fetch("./post-class-step2.html")
		.then((response) => response.text())
		.then((data) => {
			$("#content").html(data);
			reinitializeProgressBar();
		});
	formStepsNum++;
	updateProgressBar();
	// console.log(formStepsNum);
	loadFormTOFrontend();
	
}
function step3() {
	fetch("./post-class-step3.html")
		.then((response) => response.text())
		.then((data) => {
			$("#content").html(data);
		});
	formStepsNum++;
	updateProgressBar();
	// console.log(formStepsNum);
	saveFormToBackend();
}






function updateProgressBar() {
	const progressSteps = document.querySelectorAll(".progress-step");
	const progress = document.getElementById("progress");
	progressSteps.forEach((progStep, index) => {
		// console.log(index);

		if (index < formStepsNum + 1) {
			progStep.classList.add("active");
		} else {
			progStep.classList.remove("active");
		}
	});
	const progressActive = document.querySelectorAll(".progress-step.active");
	// console.log(progress);

	progress.style.width =
		((progressActive.length - 1) / (progressSteps.length - 1)) * 100 + "%";
	// console.log(progressActive);
}
function updatFormSteps(params) {
	const formSteps = document.querySelectorAll(".form-step");
	formSteps.forEach((formStep) => {
		formStep.classList.contains("active") &&
			formStep.classList.remove("active");
	});
	formSteps[formStepsNum].classList.add("active");
}

function changeAddClassButtonColor() {
	$("#addClassButton").css("display", "none");
}

function plusInput() {
	divCount++;
	dataInputCount++;
	let placeHoderText = `第${divCount}章 章節名稱`;

	courseIndex++;
	$("#formRange")
		.append(`<div id="dataInput${dataInputCount}" class="upload-group draggable" draggable="true">
                <div
                    class="flex  focus-within:outline-black col-span-2 relative  items-center rounded-md bg-white  outline outline-1 -outline-offset-1 outline-gray-300  px-4 gap-x-1">
                    <i class="hamburger-icon bi bi-list text-4xl focus:outline-none" data-id="1"></i>
                    <input class="w-[80%]  h-[3rem] text-3xl bi bi-caret-down   outline-none  " placeholder="${placeHoderText}" type="text" name="courseName${courseIndex}" autocomplete="off">
                    <button type="button" class=" absolute  right-6 w-12 flex focus:outline-none">
                        <i class="bi bi-plus-square-dotted text-4xl focus:outline-none" onclick="plusInput(dataInputCount)"></i>
                        <i class="bi bi-trash text-4xl focus:outline-none" onclick="removeInput(${dataInputCount})"></i>
                    </button>
                </div>
                <div class="mt-1 ml-auto w-[80%] flex  focus-within:outline-black col-span-2 relative  items-center rounded-md bg-white  outline outline-1 -outline-offset-1 outline-gray-300  px-4  gap-x-1 ">
                    <input type="file" accept="video/mp4" class="sr-only w-full  h-[3rem] text-2xl   outline-none  pt-1 " id="lessonLoad${dataInputCount}" name="courseVideo${courseIndex}" onchange="updateFileName(${dataInputCount})">
					<label for="lessonLoad${dataInputCount}" class=" w-full  h-[3rem] text-3xl   outline-none   cursor-pointer text-gray-400 pt-1" id="fileLabel${dataInputCount}" >請上傳課程影片
					
                </div>
                </div>`);


}
function updateFileName(dataInputCount) {
	const fileInput = document.getElementById(`lessonLoad${dataInputCount}`);
	const label = document.getElementById(`fileLabel${dataInputCount}`);

	// 檢查是否有選擇檔案
	if (fileInput.files.length > 0) {
		const fileName = fileInput.files[0].name; // 取得檔案名稱
		label.textContent = fileName; // 更新到 label
		$(`#fileLabel${dataInputCount}`).css("color", "black");
	}
}




function removeInput(dataInputId) {
	$(`#dataInput${dataInputId}`).remove();
	console.log(dataInputId);
	divCount--;
	console.log("刪除" + divCount);

	updatePlaceholders();
	if (divCount == 0) {
		$("#addClassButton").css("display", "block");
	}
}

function updatePlaceholders() {
	$('[id^="dataInput"]').each(function(index, element) {
		// 每次遍歷到一個符合條件的元素時，執行的回調
		// console.log("遍歷到 id 為 " + $(element).attr("id"));

		// 假設你想要更新這些元素內的 input 元素的 placeholder
		let newPlaceholder = "第" + (index + 1) + "章 章節名稱";
		$(element).find("input:first").attr("placeholder", newPlaceholder);
	});
}

function loadName(inputSelector, endpoint) {
	fetch(`http://localhost:8080/orders10/${endpoint}`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
	})
		.then((response) => {
			if (!response.ok) throw new Error("HTTP status" + response.status);
			return response.json();
		})
		.then((data) => $(inputSelector).val(data.data))
		.catch((error) => console.error(`${inputSelector}失敗`, error));
}

function loadImage() {
	fetch("http://localhost:8080/orders10/test23", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
	})
		.then((response) => {
			if (!response.ok) throw new Error("HTTP status" + response.status);
			return response.json();
		})
		.then((data) => $("#displayZone").attr("src", data[data.length - 1]))
		.catch((error) => console.error("Error saving form:", error));
	$("#previewText").addClass("hidden");
}

function saveFormToBackend() {
	const formHtml = $("#formRange").html(); // 獲取包含所有動態生成元素的容器的 HTML 內容
	

	// console.log(formHtml);
	fetch("http://localhost:8080/orders10/save", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify({ data: formHtml }), // 將HTML內容作為數據發送
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then((data) => {
			console.log("Form saved to backend", data);
		})
		.catch((error) => {
			console.error("Error saving form:", error);
		});
}

function loadFormTOFrontend() {
	fetch("http://localhost:8080/orders10/loadForm", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
	})
		.then((response) => {
			if (!response.ok) throw new Error("HTTP status" + response.status);
			return response.json();
		})
		.then((data) => {
			console.log(data);
			$("#formRange").html(data.data);
		})
		.catch((error) => console.error("Load form failed:", error));
}


if (!window.containerInitialized) {
  const container = document.getElementById("formRange");
//     console.log("Container 初始化狀態：", container);
// console.log("Container 中的元素清單：", container ? container.innerHTML : "不存在");


  if (container) {
    // 使用事件代理處理拖曳開始和結束
    container.addEventListener("dragstart", (event) => {
      if (event.target.classList.contains("draggable")) {
        event.target.classList.add("dragging");
      }
    });

    container.addEventListener("dragend", (event) => {
      if (event.target.classList.contains("draggable")) {
        event.target.classList.remove("dragging");
      }
      updatePlaceholders();
    });

    container.addEventListener("dragover", (event) => {
      event.preventDefault();
      const afterElement = getDragAfterElement(container, event.clientY);
      const dragging = document.querySelector(".dragging");
      
      if (afterElement == null) {
        container.appendChild(dragging);
      
      } else {
        container.insertBefore(dragging, afterElement);
        
      }
    });

    // 標記已經初始化
    window.containerInitialized = false;
  }
  
}

/**
* 找到拖曳後的目標位置
* @param {HTMLElement} container
* @param {number} y
* @returns {HTMLElement | null}
*/



function getDragAfterElement(container, y) {
  const draggableElements = [...container.querySelectorAll(".draggable:not(.dragging)")];

  return draggableElements.reduce((closest, child) => {
      const box = child.getBoundingClientRect();
      const offset = y - box.top - box.height / 2;

      if (offset < 0 && offset > closest.offset) {
          return { offset: offset, element: child };
          
      } else {
          return closest;
      }    }, { offset: Number.NEGATIVE_INFINITY }).element;
      
}





function parameterIsNull(){
fetch('http://localhost:8080/check/allParameters',{
  method:'get',
  headers: {
        "Content-Type": "application/json"
      }
  
}).then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      }).then((data) => {
      console.log("所有的值", data);
                 // 将 courseName 存储到变量中
                 var courseName = data.courseName;
                 var courseClass = data.courseClass;
                 var coursePrice = data.coursePrice;
                 var courseIntro = data.courseIntro;
                 var courseSummory = data.courseSummory;
                 var courseImage = data.courseImage;
             
             
             console.log(courseName);
                 // 如果 courseName 有值，设置输入框的值
                 if(courseName === "有值"){
                     $('#name').text("已編輯").removeAttr("href");  
             }
             if(courseClass === "有值"){
               $('#class').text("已編輯").removeAttr("href");  
             }
             if(coursePrice === "有值"){
               $('#price').text("已編輯").removeAttr("href");  
             }
             if(courseIntro === "有值"){
               $('#intro').text("已編輯").removeAttr("href");  
             }
             if(courseSummory === "有值"){
               $('#summory').text("已編輯").removeAttr("href");  
                          }
             if(courseImage === "有值"){
               $('#image').text("已編輯").removeAttr("href");  
             }
                         
      }).catch((error) => {
        console.error("Error", error);
      });
}

async function uploadFiles() {
      const formData = new FormData();
      const courseGroups = document.querySelectorAll(".upload-group");

      courseGroups.forEach(group => {
          const courseName = group.querySelector("input[type='text']").value;
    console.log(courseName)
          const courseVideo = group.querySelector("input[type='file']").files[0];
          

          // 確保名稱與影片是一對一對應，形成 "物件關係"
          if (courseName) formData.append("course_names", courseName); // 每個課程的名稱
          if (courseVideo) formData.append("course_videos", courseVideo); // 每個課程的影片
      });

      try {
    toggleOverlay(true);
          // 發送請求
          const response = await fetch('http://localhost:8080/gcs/getSignedUrl', {
              method: 'POST',
              body: formData,
          });

          if (!response.ok) throw new Error('Failed to get signed URL');

          
          const data = await response.json();

          //console.log("Response Data:", data); 
          const signedUrls = Object.keys(data).map(key => data[key].signedUrl);
          //console.log("signedUrls:", signedUrls);
          // 上傳對應的檔案
          for (let i = 0; i < signedUrls.length; i++) {
              const file = courseGroups[i].querySelector("input[type='file']").files[0];
              if (signedUrls[i] && file) {
                  await uploadFileToGCS(signedUrls[i], file);
              }
          }
    sessionStorage.setItem('uploadResult',"成功");
          alert("所有檔案上傳成功");
      } catch (error) {
    
    
      }			finally {
        // 隱藏遮罩
        toggleOverlay(false);
      }
  }

  async function uploadFileToGCS(signedUrl, file) {
      const response = await fetch(signedUrl, {
          method: 'PUT',
          body: file,
      });

      if (!response.ok) {
          throw new Error(`檔案 ${file.name} 上傳失敗`);
      }

      console.log(`檔案 ${file.name} 上傳成功`);
  }

$('#uploadButton').on('click',function(){
  uploadFiles();
})


function toggleOverlay(show) {
  const overlay = document.getElementById("upload-overlay");
  if (show) {
    overlay.classList.remove("hidden");
  } else {
    overlay.classList.add("hidden");
  }
}




