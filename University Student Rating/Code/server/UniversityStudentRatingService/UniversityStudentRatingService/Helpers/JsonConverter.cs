using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace UniversityStudentRatingService.Helpers
{
    public static class JsonConverter
    {
        public static string Convert(IEnumerable<string> dataList)
        {
            string json = JsonConvert.SerializeObject(dataList);
            return json;
        }
        public static string Convert(System.Web.Http.ModelBinding.ModelStateDictionary modelState)
        {
            List<string> errorList = modelState.Values.SelectMany(m => m.Errors).Select(e => e.ErrorMessage).ToList();
            string json = JsonConvert.SerializeObject(errorList);
            return json;
        }
    }
}