namespace DataAccessLayer.DbContext
{
    using Newtonsoft.Json;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("ProfessorDiscipline")]
    public partial class ProfessorDiscipline
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public ProfessorDiscipline()
        {
            StudentProfessorDisciplines = new HashSet<StudentProfessorDiscipline>();
        }

        public int Id { get; set; }

        public int ProfessorId { get; set; }

        public int DisciplineId { get; set; }

        public virtual Discipline Discipline { get; set; }

        public virtual UserProfile UserProfile { get; set; }

        [JsonIgnore]
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public  ICollection<StudentProfessorDiscipline> StudentProfessorDisciplines { get; set; }
    }
}
