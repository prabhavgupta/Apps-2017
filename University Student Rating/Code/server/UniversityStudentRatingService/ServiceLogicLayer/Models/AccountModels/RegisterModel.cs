using DataAccessLayer.DbContext;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ServiceLogicLayer.Models.AccountModels
{
    public class RegisterModel
    {
        [Required]
        [StringLength(100, ErrorMessage = "The {0} must be at least {2} characters long.", MinimumLength = 3)]
        [Display(Name = "UserName")]
        public string UserName { get; set; }

        [Required]
        [StringLength(100, ErrorMessage = "The {0} must be at least {2} characters long.", MinimumLength = 6)]
        [DataType(DataType.Password)]
        [Display(Name = "Password")]
        public string Password { get; set; }

        [DataType(DataType.Password)]
        [Display(Name = "Confirm password")]
        [Compare("Password", ErrorMessage = "The password and confirmation password do not match.")]
        public string ConfirmPassword { get; set; }

        [Required]
        [EmailAddress(ErrorMessage = "Incorrect email address")]
        [Display(Name = "Email")]
        public string Email { get; set; }

        [Required]
        [MinLength(2, ErrorMessage = "Minimum length is 3 characters")]
        [Display(Name = "First name")]
        public string FirstName { get; set; }

        [Required]
        [MinLength(2, ErrorMessage = "Minimum length is 3 characters")]
        [Display(Name = "Last name")]
        public string LastName { get; set; }

        [Required(ErrorMessage = "Gender is required")]
        [Display(Name = "GenderTypeId")]
        public int GenderTypeId { get; set; }

        [Required(ErrorMessage = "Country is required")]
        [Display(Name = "CountryId")]
        public int CountryId { get; set; }

        [Required(ErrorMessage = "City is required")]
        [Display(Name = "CityId")]
        public int CityId { get; set; }

        [Required(ErrorMessage = "University is required")]
        [Display(Name = "UniversityId")]
        public int UniversityId { get; set; }

        [Required(ErrorMessage = "Status is required")]
        [Display(Name = "StatusId")]
        public int StatusId { get; set; }

        [Display(Name = "FacultyId")]
        public int? FacultyId { get; set; }

        [Display(Name = "CourseId")]
        public int? CourseId { get; set; }
        
        [Display(Name = "SpecialKEY")]
        public string SpecialKEY { get; set; }
    }
}