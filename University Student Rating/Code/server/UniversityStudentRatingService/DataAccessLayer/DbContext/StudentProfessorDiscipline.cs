namespace DataAccessLayer.DbContext
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("StudentProfessorDiscipline")]
    public partial class StudentProfessorDiscipline
    {
        public int Id { get; set; }

        public int StudentId { get; set; }

        public int ProfessorDisciplineId { get; set; }

        public virtual ProfessorDiscipline ProfessorDiscipline { get; set; }

        public virtual UserProfile UserProfile { get; set; }
    }
}
