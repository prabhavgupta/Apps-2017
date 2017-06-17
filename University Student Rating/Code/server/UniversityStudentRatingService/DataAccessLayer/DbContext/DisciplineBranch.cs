namespace DataAccessLayer.DbContext
{
    using Newtonsoft.Json;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("DisciplineBranch")]
    public partial class DisciplineBranch
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public DisciplineBranch()
        {
            Disciplines = new HashSet<Discipline>();
        }

        public int Id { get; set; }

        [Required]
        public string Name { get; set; }

        public string IconPath { get; set; }

        [JsonIgnore]
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public  ICollection<Discipline> Disciplines { get; set; }
    }
}
