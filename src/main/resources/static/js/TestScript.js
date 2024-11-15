
const apiUrltest = 'http://localhost:8080/api/test';

export async function testFetch(){
	
	fetch(apiUrltest).then(response =>{
		if (!response.ok){
			throw new Error('Network response');	
		}
		return response.json();
	}).then(data =>{
		console.log('Data received:' , data);
	}).catch(error =>{
		console.error('Three has been a proble with your fetch operation',error)
	})

}

// doSomething().then(function(result){
// 	return doSomethingElse(result);})
// 	.then(function(newResult){
// 	return doThirdThing(newResult);})
// 	.then(function(finalResult){
// 	console.log('final result:' + finalResult)})

