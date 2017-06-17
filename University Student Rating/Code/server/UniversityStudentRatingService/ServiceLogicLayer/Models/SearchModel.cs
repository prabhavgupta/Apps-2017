using DataAccessLayer.DbContext;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ServiceLogicLayer.Models
{
    public class SearchModel
    {
        public int SkipCount { get; set; }
        public int TakeCount { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public int? GenderTypeId { get; set; }
        public int? CountryId { get; set; }
        public int? CityId { get; set; }
        public int? UniversityId { get; set; }
        public int? StatusId { get; set; }
        public int? FacultyId { get; set; }
        public int? CourseId { get; set; }
    }
}