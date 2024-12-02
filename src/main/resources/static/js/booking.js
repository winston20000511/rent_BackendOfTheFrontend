var url = window.location.pathname.split('/');
var houseId = url[url.length - 1];
var time;
var day;
var month;
var year;
var months = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"];
var center;

var weekDay;
var fromDate;
var toDate;
var fromTime;
var toTime;
var duration;


$.ajax({
	url: '/api/booking/add',
	type: 'GET',
	dataType: 'json',
	data: {
		houseId: houseId,
	},
	success: function(response) {
		console.log(response);
		weekDay = response.weekDay;
		fromDate = response.fromDate;
		toDate = response.toDate;
		fromTime = response.fromTime;
		toTime = response.toTime;
		duration = response.duration;

		const owl = $(".timepicker .owl");
		owl.empty();

		var startTime = new Date(`1970-01-01T${fromTime}`);
		var endTime = new Date(`1970-01-01T${toTime}`);

		while (startTime < endTime) {
			var nextTime = new Date(startTime.getTime() + duration * 60000); // duration 以分鐘為單位
			var timeSlot = `${startTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false })}`;

			owl.append(`<div>${timeSlot}</div>`);

			startTime = nextTime;
		}

		todayEqualActive();

		// datepicker 設定中文化
		$.datepicker.regional['zh-TW'] = {
			clearText: '清除',
			closeText: '關閉',
			prevText: '< 上個月',
			nextText: '下個月 >',
			currentText: '今天',
			monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
			monthNamesShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "十二"],
			dayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
			dayNamesShort: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
			dayNamesMin: ['日', '一', '二', '三', '四', '五', '六'],
			dateFormat: 'yy-mm-dd',
			firstDay: 1
		};
		$.datepicker.setDefaults($.datepicker.regional['zh-TW']);


		$('#calendar').datepicker({
			inline: true,
			firstDay: 1,
			minDate: fromDate,
			maxDate: toDate,
			showOtherMonths: true,
			beforeShowDay: function(date) {
				var day = date.getDay();
				var convertedDay = (day === 0) ? 6 : (day - 1);
				var isDisabled = weekDay.charAt(convertedDay) === '0'; // 如果該日為 '0'，表示禁用

				return [!isDisabled, ""]; // 如果 isDisabled 為 true，則禁用該日期
			},
			dateFormat: 'yy-mm-dd',
			onChangeMonthYear: function() {
				todayEqualActive();
			},
			onSelect: function(dateText, inst) {
				var date = $(this).datepicker('getDate'),
					day = date.getDate(),
					month = date.getMonth() + 1,
					year = date.getFullYear();

				// display day and month on submit button
				var monthName = months[month - 1];
				$(".request .day").text(year + "年" + monthName + " " + day + "日");

				todayEqualActive();

				$(".request").removeClass("disabled");

				var index;

				setTimeout(function() {
					$(".ui-datepicker-calendar tbody tr").each(function() {
						if ($(this).find(".ui-datepicker-current-day").length) {
							index = $(this).index() + 1;
						}
					});

					// insert timepiker placeholder after selected row
					$("<tr class='timepicker-cf'></tr>")
						.insertAfter($(".ui-datepicker-calendar tr")
							.eq(index));

					var top = $(".timepicker-cf").offset().top - 2;

					if ($(".timepicker").css('height') == '60px') {
						$(".timepicker-cf").animate({
							'height': '0px'
						}, { duration: 200, queue: false });
						$(".timepicker").animate({
							'top': top
						}, 200);
						$(".timepicker-cf").animate({
							'height': '60px'
						}, 200);
					}
					else {
						$(".timepicker").css('top', top);
						$(".timepicker, .timepicker-cf").animate({
							'height': '60px'
						}, 200);
					}
				}, 0);

				// display time on submit button
				time = $(".owl-stage .center").text();
				$(".request .time").text(getTimeSlot(time, duration));


				$(".owl-item").removeClass("center-n");
				center = $(".owl-stage").find(".center");
				center.prev("div").addClass("center-n");
				center.next("div").addClass("center-n");
			}
		});

		// if the inputs arent empty force ":focus state"
		$(".form-name input").each(function() {
			var $input = $(this);
			var $label = $input.siblings("label");

			// 初始檢查，如果輸入框有值，則移動標籤
			if ($input.val()) {
				$label.css({
					'font-size': '0.8em',
					'left': '.15rem',
					'top': '0%'
				});
			}

			// 當用戶在輸入框中鍵入時
			$input.on('keyup', function() {
				if ($input.val()) {
					// 如果有值，保持標籤樣式
					$label.css({
						'font-size': '0.8em',
						'left': '.15rem',
						'top': '0%'
					});
				} else {
					// 如果沒有值，移除樣式
					$label.removeAttr("style");
				}
			});
		});

		$(".timepicker").on('click', '.owl-next', function() {
			updateTimeAndCenter();
		});

		$(".timepicker").on('click', '.owl-prev', function() {
			updateTimeAndCenter();
		});

		$(".timepicker").on('click', '.owl-item', function() {
			$(".owl-carousel").trigger("to.owl.carousel", [$(this).index(), 300]); // 移動到點擊的項目
			setTimeout(updateTimeAndCenter, 350);
		});


		$('.owl').owlCarousel({
			center: true,
			loop: true,
			items: 5,
			dots: false,
			nav: true,
			navText: " ",
			mouseDrag: false,
			touchDrag: true,
			responsive: {
				0: {
					items: 3
				},
				700: {
					items: 5
				},
				1200: {
					items: 7
				}
			}
		});

		$(document).on('click', '.ui-datepicker-next', function(e) {
			$(".timepicker-cf").hide(0);
			$(".timepicker").css({
				'height': '0'
			});
			e.preventDefault();
			$(".ui-datepicker").animate({
				"-webkit-transform": "translate(100%,0)"
			}, 200);
		});

		$(document).on('click', '.ui-datepicker-prev', function() {
			$(".timepicker-cf").hide(0);
			$(".timepicker").css({
				'height': '0'
			});
			$(".ui-datepicker").animate({
				'transform': 'translateX(-100%)'
			}, 200);
		});

		$(window).on('resize', function() {
			var timepickerCf = $(".timepicker-cf");

			if (timepickerCf.length > 0) {
				$(".timepicker").css('top', timepickerCf.offset().top - 2);
			}
		});

	},
	errer: function(err) {
		console.log("ERR!!!!!!!!!!!!!!!");
	}
});


$(".request").on("click", function(e) {
	e.preventDefault();

	var name = $("#name").val();
	var email = $("#email").val();

	if (name === "" || email === "") {
		alert("請填寫姓名和email！");
		return;
	}

	var date = $(".request .day").text();
	var time = $(".request .time").text();

	// 確認日期和時間是否選擇
	if (date === "" || time === "") {
		alert("請選擇預約日期和時段！");
		return;
	}

	var formData = {
		name: name,
		email: email,
		date: date,
		time: time
	};

	$.ajax({
		url: '/api/house/book', 
		type: 'POST',
		data: formData,
		success: function(response) {
			alert("預約成功！"); 
		},
		error: function(xhr, status, error) {
			alert("提交失敗，請稍後再試！");
		}
	});

});



// 如果所選日期是今天的日期，則刪除邊框
function todayEqualActive() {
	setTimeout(function() {
		if ($(".ui-datepicker-current-day").hasClass("ui-datepicker-today")) {
			$(".ui-datepicker-today")
				.children(".ui-state-default")
				.css("border-bottom", "0");
		}
		else {
			$(".ui-datepicker-today")
				.children(".ui-state-default")
				.css("border-bottom", "2px solid rgba(53,60,66,0.5)");
		}
	}, 20);
}


function getTimeSlot(time, duration) {
	// 將 time 拆解為小時和分鐘
	const timeParts = time.split(":");
	const hour = parseInt(timeParts[0], 10);
	const minute = parseInt(timeParts[1], 10);

	// 將時間轉換成 Date 物件
	const startTime = new Date(1970, 0, 1, hour, minute);

	// 計算結束時間
	const endTime = new Date(startTime.getTime() + duration * 60000);

	// 格式化時間顯示
	const formatTime = (date) =>
		date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit", hour12: false });

	// 返回時間段
	return `${formatTime(startTime)} - ${formatTime(endTime)}`;
}


function updateTimeAndCenter() {
	time = $(".owl-stage .center").text(); // 取得中心的時間
	$(".request .time").text(getTimeSlot(time, duration)); // 更新時間顯示

	// 更新中心項目和鄰近項目的樣式
	$(".owl-item").removeClass("center-n");
	center = $(".owl-stage").find(".center");
	center.prev("div").addClass("center-n");
	center.next("div").addClass("center-n");
}

