namespace DataAccessLayer.DbContext
{
    using Newtonsoft.Json;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("UserProfile")]
    public partial class UserProfile
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public UserProfile()
        {
            ProfessorDisciplines = new HashSet<ProfessorDiscipline>();
            Ratings = new HashSet<Rating>();
            Ratings1 = new HashSet<Rating>();
            StudentProfessorDisciplines = new HashSet<StudentProfessorDiscipline>();
        }

        public int Id { get; set; }

        [Required]
        [StringLength(50)]
        public string FirstName { get; set; }

        [Required]
        [StringLength(50)]
        public string LastName { get; set; }

        public string PhotoPath { get; set; }

        public int? FacultyId { get; set; }

        public int? CourseId { get; set; }

        public int GenderTypeId { get; set; }

        public int CountryId { get; set; }

        public int CityId { get; set; }

        public int UniversityId { get; set; }

        public int StatusId { get; set; }

        public virtual City City { get; set; }

        public virtual Country Country { get; set; }

        public virtual Course Course { get; set; }

        public virtual Faculty Faculty { get; set; }

        public virtual GenderType GenderType { get; set; }

        [JsonIgnore]
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public ICollection<ProfessorDiscipline> ProfessorDisciplines { get; set; }

        [JsonIgnore]
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public ICollection<Rating> Ratings { get; set; }

        [JsonIgnore]
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public ICollection<Rating> Ratings1 { get; set; }

        public virtual Status Status { get; set; }

        [JsonIgnore]
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public ICollection<StudentProfessorDiscipline> StudentProfessorDisciplines { get; set; }

        public virtual University University { get; set; }
    }
}
